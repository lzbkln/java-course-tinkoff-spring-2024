package edu.java.service;

import edu.java.dto.requests.AddLinkRequest;
import edu.java.dto.requests.RemoveLinkRequest;
import edu.java.dto.responses.LinkResponse;
import edu.java.dto.responses.ListLinksResponse;
import edu.java.repository.LinkRepository;
import edu.java.repository.LinkageTableRepository;
import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.LinkageTable;
import edu.java.service.exceptions.AlreadyTrackedLinkException;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final TelegramChatRepository telegramChatRepository;
    private final LinkageTableRepository linkageTableRepository;

    public ListLinksResponse getAllLinks(Long chatId) {
        checkRegisterChat(chatId);

        List<LinkageTable> linkageTables = linkageTableRepository.findByChatId(chatId);
        List<LinkResponse> linkResponses = linkageTables.stream()
            .map(LinkageTable::getLinkId)
            .map(linkRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(link -> new LinkResponse(link.getLinkId(), URI.create(link.getUrl())))
            .collect(Collectors.toList());

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    public void saveLink(Long chatId, AddLinkRequest addLinkRequest) {
        checkRegisterChat(chatId);

        List<LinkageTable> linkageTables = linkageTableRepository.findByChatId(chatId);
        List<Link> chatLinks = linkageTables.stream()
            .map(LinkageTable::getLinkId)
            .map(linkRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        Optional<Link> linkToSave = chatLinks.stream()
            .filter(chatLink -> chatLink.getUrl().equals(addLinkRequest.link().toString()))
            .findFirst();

        if (linkToSave.isPresent()) {
            throw new AlreadyTrackedLinkException(addLinkRequest.link());
        }

        linkRepository.save(new Link(addLinkRequest.link().toString()));
        LinkageTable linkageTable =
            new LinkageTable(chatId, linkRepository.findByUrl(addLinkRequest.link().toString()).get().getLinkId());
        linkageTableRepository.save(linkageTable);
    }

    //TODO проверка на то, используется ли где-то ссылка. если нет удалить из links
    public void deleteLinkFromChat(Long chatId, RemoveLinkRequest request) {
        checkRegisterChat(chatId);

        List<LinkageTable> linkageTables = linkageTableRepository.findByChatId(chatId);
        List<Link> chatLinks = linkageTables.stream()
            .map(LinkageTable::getLinkId)
            .map(linkRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        Optional<Link> linkToDelete = chatLinks.stream()
            .filter(chatLink -> chatLink.getUrl().equals(request.link().toString()))
            .findFirst();

        if (linkToDelete.isEmpty()) {
            throw new NoSuchLinkException(request.link());
        }

        Long linkIdToDelete = linkToDelete.get().getLinkId();

        linkageTables.stream()
            .filter(linkageTable -> Objects.equals(linkageTable.getLinkId(), linkIdToDelete))
            .findFirst()
            .ifPresent(linkageTable -> linkageTableRepository.removeByChatIdAndLinkId(
                chatId,
                linkageTable.getLinkId()
            ));
    }

    private void checkRegisterChat(Long chatId) {
        telegramChatRepository.findById(chatId).orElseThrow(() -> new NonRegisterChatException(chatId));
    }
}
