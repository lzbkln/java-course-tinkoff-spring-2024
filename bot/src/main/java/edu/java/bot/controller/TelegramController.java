package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TelegramController implements UpdatesListener {
    public final CommandService commandService;
    public final TelegramBot telegramBot;

    public TelegramController(TelegramBot telegramBot, CommandService commandService) {
        telegramBot.setUpdatesListener(this);
        this.telegramBot = telegramBot;
        this.commandService = commandService;
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> {
            if (update.message() != null) {
                Long chatId = update.message().chat().id();
                SendMessage sendMessage =
                    new SendMessage(chatId, commandService.doCommand(update));
                telegramBot.execute(sendMessage);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
