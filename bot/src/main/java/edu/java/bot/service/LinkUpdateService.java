package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdateService {
    private final TelegramBot telegramBot;

    public void sendMessage(List<Long> tgChatIds, String description) {
        tgChatIds.forEach(chatId -> {
            telegramBot.execute(new SendMessage(chatId, description));
        });
    }
}
