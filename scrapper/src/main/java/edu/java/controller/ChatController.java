package edu.java.controller;

import edu.java.dto.requests.AddLinkRequest;
import edu.java.dto.requests.RemoveLinkRequest;
import edu.java.dto.responses.LinkResponse;
import edu.java.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/scrapper/tg-chat/{id}")
@RequiredArgsConstructor
public class ChatController implements ChatControllerAnnotations{
    private final TelegramChatService telegramChatService;
    @PostMapping
    public void registerChat(@PathVariable Long id) {
        telegramChatService.addNewChat(id);
        ResponseEntity.ok().build();
    }

    @DeleteMapping
    public void deleteChat(@PathVariable Long id) {
        telegramChatService.deleteChat(id);
        ResponseEntity.ok().build();
    }
}
