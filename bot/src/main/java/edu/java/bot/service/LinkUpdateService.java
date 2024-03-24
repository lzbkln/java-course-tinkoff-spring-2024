package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.requests.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdateService {
    private final TelegramBot telegramBot;

    public void sendMessage(LinkUpdateRequest request) {
        request.tgChatIds().forEach(chatId -> {
            String message = request.description();
            telegramBot.execute(new SendMessage(chatId, message));
        });
    }
}
