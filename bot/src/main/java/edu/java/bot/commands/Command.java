package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import java.net.URISyntaxException;

public interface Command {
    String execute(Update update) throws URISyntaxException;

    String nameCommand();

    String descriptionOfCommand();

    BotCommand getBotCommand();
}
