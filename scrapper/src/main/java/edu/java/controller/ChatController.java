package edu.java.controller;

import edu.java.service.TelegramChatService;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapper/tg-chat")
@RequiredArgsConstructor
public class ChatController implements ChatApi {
    private final TelegramChatService telegramChatService;
    private final Counter messagesCounter;

    @PostMapping("/{id}")
    public void registerChat(@PathVariable Long id) {
        messagesCounter.increment();
        telegramChatService.register(id);
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable Long id) {
        messagesCounter.increment();
        telegramChatService.unregister(id);
    }
}
