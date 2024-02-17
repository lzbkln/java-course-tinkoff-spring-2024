package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component("/help")
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION_OF_COMMAND = "Get list of all commands";

    private static final String RESPONSE = """
        This bot works as a single update tracking method for many resources.
        First you need to register:
        /start - Register user
        All commands:
        /track - Start link tracking
        /untrack - Stop link tracking
        /list - Show list of tracked links
        /help - Get list of all commands""";

    @Override
    public String execute(Update update) {
        return RESPONSE;
    }

    @Override
    public String nameCommand() {
        return COMMAND;
    }

    @Override
    public String descriptionOfCommand() {
        return DESCRIPTION_OF_COMMAND;
    }

    @Override
    public BotCommand getBotCommand() {
        return new BotCommand(nameCommand(), descriptionOfCommand());
    }

}
