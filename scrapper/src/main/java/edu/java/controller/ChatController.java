package edu.java.controller;

import edu.java.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapper/tg-chat")
@RequiredArgsConstructor
public class ChatController implements ChatControllerAnnotations {
    private final TelegramChatService telegramChatService;

    @PostMapping("/{id}")
    public void registerChat(@PathVariable Long id) {
        telegramChatService.addNewChat(id);
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable Long id) {
        telegramChatService.deleteChat(id);
    }
}
