package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.entities.User;
import edu.java.bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/start")
@RequiredArgsConstructor
public class StartCommand implements Command {
    public final UserRepository userRepository;
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION_OF_COMMAND = "Register user";
    private static final String SUCCESS_REGISTER_MESSAGE = "Successful registration";

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
        User user = new User(id);
        userRepository.saveUser(user);
        return SUCCESS_REGISTER_MESSAGE;
    }
}
