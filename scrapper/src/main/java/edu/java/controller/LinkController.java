package edu.java.controller;

import edu.java.dto.requests.AddLinkRequest;
import edu.java.dto.requests.RemoveLinkRequest;
import edu.java.dto.responses.ListLinksResponse;
import edu.java.service.LinkService;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapper/links")
@RequiredArgsConstructor
public class LinkController implements LinkApi {
    private final LinkService linkService;
    private final Counter messagesCounter;

    @PostMapping
    public void addLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody AddLinkRequest addLinkRequest) {
        messagesCounter.increment();
        linkService.saveLink(id, addLinkRequest.link());
    }

    @GetMapping
    public ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long chatId) {
        messagesCounter.increment();
        return linkService.getAllLinksResponse(chatId);
    }

    @DeleteMapping
    public void deleteLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody RemoveLinkRequest request) {
        messagesCounter.increment();
        linkService.deleteLink(id, request.link());
    }
}
