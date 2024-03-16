package edu.java.service.jdbc;

import edu.java.dto.responses.LinkResponse;
import edu.java.dto.responses.ListLinksResponse;
import edu.java.repository.LinkRepository;
import edu.java.repository.LinkageRepository;
import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.Linkage;
import edu.java.service.LinkService;
import edu.java.service.exceptions.AlreadyTrackedLinkException;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final TelegramChatRepository telegramChatRepository;
    private final LinkageRepository linkageTableRepository;

    @Override
    public void saveLink(Long tgChatId, URI url) {
        checkRegisterChat(tgChatId);
        try {
            Link link = new Link(url.toString());
            linkRepository.save(link);
            linkageTableRepository.save(new Linkage(
                tgChatId,
                linkRepository.findByUrl(url.toString()).getId()
            ));
        } catch (DuplicateKeyException e) {
            throw new AlreadyTrackedLinkException(url);
        }
    }

    @Override
    public void deleteLink(Long tgChatId, URI url) {
        checkRegisterChat(tgChatId);
        try {
            Link link = linkRepository.findByUrl(url.toString());
            Long linkId = link.getId();
            linkageTableRepository.removeByChatIdAndLinkId(tgChatId, linkId);
            if (linkageTableRepository.countByLinkId(linkId) == 0) {
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
        try {
            telegramChatRepository.findById(chatId);
        } catch (EmptyResultDataAccessException e) {
            throw new NonRegisterChatException(chatId);
        }
    }
}
