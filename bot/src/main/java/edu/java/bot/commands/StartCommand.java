package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.ScrapperChatClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component("/start")
@RequiredArgsConstructor
public class StartCommand implements Command {
    public final ScrapperChatClient scrapperChatClient;
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION_OF_COMMAND = "Register user";
    private static final String SUCCESS_REGISTER_MESSAGE = "Successful registration";
    private static final String NO_SUCCESS_REGISTER_MESSAGE = "Double registration!";

    @Override
    public String execute(Update update) {
        return registration(update.message().chat().id());
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
        return new BotCommand(COMMAND, DESCRIPTION_OF_COMMAND);
    }

    public String registration(Long id) {
        return scrapperChatClient.registerChat(id).map(
            response -> {
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    return SUCCESS_REGISTER_MESSAGE;
                }
                return null;
            }
        ).onErrorReturn(NO_SUCCESS_REGISTER_MESSAGE).block();

    }
}
