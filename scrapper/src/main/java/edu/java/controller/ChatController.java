package edu.java.controller;

import edu.java.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapper/tg-chat")
@RequiredArgsConstructor
public class ChatController {
    private final TelegramChatService telegramChatService;

    @PostMapping
    public void registerChat(@RequestHeader("Tg-Chat-Id") Long id) {
        telegramChatService.addNewChat(id);
        ResponseEntity.ok().build();
    }

    @DeleteMapping
    public void deleteChat(@RequestHeader("Tg-Chat-Id") Long id) {
        telegramChatService.deleteChat(id);
        ResponseEntity.ok().build();
    }
}
