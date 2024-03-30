package edu.java.service.jooq;

import edu.java.clients.sites.util.Utils;
import edu.java.dto.responses.GithubBranchResponseDTO;
import edu.java.dto.responses.LinkResponse;
import edu.java.dto.responses.ListLinksResponse;
import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.LinkageRepository;
import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.GithubBranches;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.Linkage;
import edu.java.repository.entity.StackOverflowQuestion;
import edu.java.repository.entity.TelegramChat;
import edu.java.service.LinkService;
import edu.java.service.exceptions.AlreadyTrackedLinkException;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final TelegramChatRepository telegramChatRepository;
    private final LinkageRepository linkageTableRepository;
    private final GithubBranchesRepository githubBranchesRepository;
    private final StackOverflowQuestionRepository stackOverflowQuestionRepository;
    private final Utils clientUtil;
    private static final String GITHUB_HOST = "github.com";
    private static final String STACK_OVERFLOW_HOST = "stackoverflow.com";

    @Override
    public void saveLink(Long tgChatId, URI url) {
        checkRegisterChat(tgChatId);
        checkAlreadyTrackedLinks(tgChatId, url);

        if (linkRepository.findByUrlBool(url.toString())) {
            saveLinkage(tgChatId, linkRepository.findByUrl(url.toString()).get().getId());
        } else {
            Link newLink = new Link(url.toString());
            Long linkId = saveNewLink(newLink);

            saveLinkage(tgChatId, linkId);
            saveAdditionalData(linkId, url);

        }
    }

    @Override
    public void deleteLink(Long tgChatId, URI url) {
        checkRegisterChat(tgChatId);
        try {
            Optional<Link> link = linkRepository.findByUrl(url.toString());
            Long linkId = link.get().getId();
            linkageTableRepository.removeByChatIdAndLinkId(tgChatId, linkId);
            if (linkageTableRepository.countByLinkId(linkId) == 0) {
                removeAdditionalData(linkId, url);
                linkRepository.removeById(linkId);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchLinkException(url);
        }
    }

    @Override
    public ListLinksResponse getAllLinksResponse(Long tgChatId) {
        checkRegisterChat(tgChatId);

        List<Link> links = linkageTableRepository.findByChatId(tgChatId).stream()
            .map(Linkage::getLinkId)
            .map(linkRepository::findById)
            .toList();

        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
            .collect(Collectors.toList());
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    private void checkRegisterChat(Long chatId) {
        TelegramChat telegramChat = telegramChatRepository.findById(chatId);
        if (telegramChat == null) {
            throw new NonRegisterChatException(chatId);
        }
    }

    private void checkAlreadyTrackedLinks(Long tgChatId, URI url) {
        Optional<Link> link = linkRepository.findByUrl(url.toString());
        if (link.isPresent()) {
            if (linkageTableRepository.findByLinkIdAndChatId(link.get().getId(), tgChatId)) {
                throw new AlreadyTrackedLinkException(url);
            }
        }
    }

    private void saveLinkage(Long tgChatId, Long linkId) {
        linkageTableRepository.save(new Linkage(tgChatId, linkId));
    }

    private Long saveNewLink(Link link) {
        linkRepository.save(link);
        return linkRepository.findByUrl(link.getUrl()).get().getId();
    }

    private void saveAdditionalData(Long linkId, URI url) {
        if (url.getHost().equals(GITHUB_HOST)) {
            clientUtil.getBranches(url.toString())
                .map(response -> {
                    List<String> branches = Arrays.stream(response)
                        .map(GithubBranchResponseDTO::name)
                        .collect(Collectors.toList());
                    return new GithubBranches(linkId, branches);
                })
                .doOnSuccess(githubBranchesRepository::save)
                .subscribe();
        }
        if (url.getHost().equals(STACK_OVERFLOW_HOST)) {
            stackOverflowQuestionRepository.save(new StackOverflowQuestion(
                linkId,
                clientUtil.getAnswerCount(url.toString())
            ));
        }
    }

    private void removeAdditionalData(Long linkId, URI url) {
        if (url.getHost().equals(GITHUB_HOST)) {
            githubBranchesRepository.removeByLinkId(linkId);
        }
        if (url.getHost().equals(STACK_OVERFLOW_HOST)) {
            stackOverflowQuestionRepository.removeByLinkId(linkId);
        }
    }
}
