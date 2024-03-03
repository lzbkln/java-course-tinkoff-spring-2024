package edu.java.service;

import edu.java.dto.requests.AddLinkRequest;
import edu.java.dto.requests.RemoveLinkRequest;
import edu.java.dto.responses.LinkResponse;
import edu.java.entity.Link;
import edu.java.entity.TelegramChat;
import edu.java.repository.LinkRepository;
import edu.java.repository.TelegramChatRepository;
import edu.java.service.exceptions.AlreadyTrackedLinkException;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final TelegramChatRepository telegramChatRepository;

    public List<LinkResponse> getAllLinks(Long chatId) {
        checkRegisterChat(chatId);

        List<Link> links = telegramChatRepository.findById(chatId).getLinks();
        List<LinkResponse> linkResponses = new ArrayList<>();

        for (Link link : links) {
            LinkResponse linkResponse = new LinkResponse(link.id(), link.url());
            linkResponses.add(linkResponse);
        }

        return linkResponses;
    }

    public void saveLink(Long chatId, AddLinkRequest addLinkRequest) {
        checkRegisterChat(chatId);

        TelegramChat telegramChat = telegramChatRepository.findById(chatId);
        List<Link> chatLinks = new ArrayList<>(telegramChat.getLinks());

        if (chatLinks.stream().anyMatch(link -> link.url().equals(addLinkRequest.link()))) {
            throw new AlreadyTrackedLinkException(addLinkRequest.link(), chatId);
        } else {
            Link newLink = linkRepository.save(new Link(new Random().nextLong(), addLinkRequest.link()));
            chatLinks.add(newLink);
            telegramChat.setLinks(chatLinks);
            telegramChatRepository.saveUser(telegramChat);
        }
    }

    public void deleteLinkFromChat(Long chatId, RemoveLinkRequest request) {
        checkRegisterChat(chatId);

        TelegramChat telegramChat = telegramChatRepository.findById(chatId);
        List<Link> chatLinks = new ArrayList<>(telegramChat.getLinks());

        if (chatLinks.stream().noneMatch(link -> link.url().equals(request.link()))) {
            throw new NoSuchLinkException(request.link(), chatId);
        }

        chatLinks.removeIf(chatLink -> chatLink.url().equals(request.link()));

        telegramChat.setLinks(chatLinks);
        telegramChatRepository.saveUser(telegramChat);
    }

    private void checkRegisterChat(Long chatId) {
        if (telegramChatRepository.findById(chatId) == null) {
            throw new NonRegisterChatException(chatId);
        }
    }
}
