package edu.java.service.jpa;

import edu.java.clients.sites.util.Utils;
import edu.java.dto.responses.GithubBranchResponseDTO;
import edu.java.dto.responses.LinkResponse;
import edu.java.dto.responses.ListLinksResponse;
import edu.java.repository.jpa.JpaGithubBranchesRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaLinkageRepository;
import edu.java.repository.jpa.JpaStackOverflowQuestionRepository;
import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.repository.jpa.entity.JpaGithubBranches;
import edu.java.repository.jpa.entity.JpaLink;
import edu.java.repository.jpa.entity.JpaLinkage;
import edu.java.repository.jpa.entity.JpaStackOverflowQuestion;
import edu.java.repository.jpa.entity.JpaTelegramChat;
import edu.java.service.LinkService;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final Utils clientUtil;
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaLinkageRepository jpaLinkageRepository;
    private final JpaGithubBranchesRepository jpaGithubBranchesRepository;
    private final JpaTelegramChatRepository jpaTelegramChatRepository;
    private final JpaStackOverflowQuestionRepository jpaStackOverflowQuestionRepository;
    private static final String GITHUB_HOST = "github.com";
    private static final String STACK_OVERFLOW_HOST = "stackoverflow.com";

    @Override
    public void saveLink(Long tgChatId, URI url) {
        checkRegisterChat(tgChatId);

        if (jpaLinkRepository.findByUrlBool(url.toString())) {
            saveLinkage(tgChatId, jpaLinkRepository.findByUrl(url.toString()).get());
        } else {
            JpaLink newLink = new JpaLink(url.toString());
            JpaLink linkId = saveNewLink(newLink);

            saveLinkage(tgChatId, linkId);
            saveAdditionalData(linkId, url);
        }
    }

    @Override
    public void deleteLink(Long tgChatId, URI url) {
        //не удалилось из таблицы ссылок стэк
        checkRegisterChat(tgChatId);

        JpaLink link = jpaLinkRepository.findByUrl(url.toString()).orElseThrow(() -> new NoSuchLinkException(url));
        Long linkId = link.getId();
        jpaLinkageRepository.deleteByLinkIdAndChatId(linkId, tgChatId);
        if (jpaLinkageRepository.countByLinkId(linkId) == 0) {
            removeAdditionalData(linkId, url);
            jpaLinkRepository.deleteById(linkId);
        }

    }

    @Override
    public ListLinksResponse getAllLinksResponse(Long tgChatId) {
        checkRegisterChat(tgChatId);

        List<JpaLink> links = jpaLinkageRepository.findByChatId(tgChatId).stream()
            .map(JpaLinkage::getLinkId)
            .toList();

        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
            .collect(Collectors.toList());
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    private void checkRegisterChat(Long chatId) {
        jpaTelegramChatRepository.findById(chatId).orElseThrow(() -> new NonRegisterChatException(chatId));
    }

    private void saveLinkage(Long tgChatId, JpaLink link) {
        jpaLinkageRepository.save(new JpaLinkage(new JpaTelegramChat(tgChatId), link));
    }

    private JpaLink saveNewLink(JpaLink link) {
        jpaLinkRepository.saveAndFlush(link);
        return jpaLinkRepository.findByUrl(link.getUrl()).get();
    }

    private void saveAdditionalData(JpaLink link, URI url) {
        if (url.getHost().equals(GITHUB_HOST)) {
            clientUtil.getBranches(url.toString())
                .map(response -> {
                    List<String> branches = Arrays.stream(response)
                        .map(GithubBranchResponseDTO::name)
                        .collect(Collectors.toList());
                    return new JpaGithubBranches(link, branches);
                })
                .doOnSuccess(jpaGithubBranchesRepository::saveAndFlush)
                .subscribe();
        }
        if (url.getHost().equals(STACK_OVERFLOW_HOST)) {
            jpaStackOverflowQuestionRepository.saveAndFlush(new JpaStackOverflowQuestion(
                link,
                clientUtil.getAnswerCount(url.toString())
            ));
        }
    }

    private void removeAdditionalData(Long linkId, URI url) {
        if (url.getHost().equals(GITHUB_HOST)) {
            jpaGithubBranchesRepository.deleteByLinkId(new JpaLink(linkId));
        }
        if (url.getHost().equals(STACK_OVERFLOW_HOST)) {
            jpaStackOverflowQuestionRepository.deleteByLinkId(new JpaLink(linkId));
        }
    }
}
