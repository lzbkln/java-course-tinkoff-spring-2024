package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/help")
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION_OF_COMMAND = "Get list of all commands";

    @Autowired
    @Qualifier("/start")
    private Command startCommand;

    @Autowired
    @Qualifier("/list")
    private Command listCommand;

    @Autowired
    @Qualifier("/track")
    private Command trackCommand;

    @Autowired
    @Qualifier("/untrack")
    private Command untrackCommand;

    @Override
    public String execute(Update update) {
        return String.join(
            "\n",
            "This bot works as a single update tracking method for many resources.",
            "First you need to register:",
            formatCommandDescription(startCommand),
            "All commands:",
            formatCommandDescription(trackCommand),
            formatCommandDescription(untrackCommand),
            formatCommandDescription(listCommand),
            formatCommandDescription(this)
        );
    }

    private String formatCommandDescription(Command command) {
        return String.format("%s - %s\n", command.nameCommand(), command.descriptionOfCommand());
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
