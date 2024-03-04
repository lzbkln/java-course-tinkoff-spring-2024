package edu.java.controller;

import edu.java.dto.requests.AddLinkRequest;
import edu.java.dto.requests.RemoveLinkRequest;
import edu.java.dto.responses.LinkResponse;
import edu.java.service.LinkService;
import java.util.List;
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
public class LinkController implements LinkControllerAnnotations {
    private final LinkService linkService;

    @PostMapping
    public void addLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody AddLinkRequest addLinkRequest) {
        linkService.saveLink(id, addLinkRequest);
    }

    @GetMapping
    public List<LinkResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") Long chatId) {
        return linkService.getAllLinks(chatId);
    }

    @DeleteMapping
    public void deleteLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody RemoveLinkRequest request) {
        linkService.deleteLinkFromChat(id, request);
    }
}
