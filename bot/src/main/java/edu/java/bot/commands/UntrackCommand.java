package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.entities.User;
import edu.java.bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/untrack")
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    public final UserRepository userRepository;
    private static final String INVALID_FORMAT_MESSAGE = "Invalid format. Please use: /untrack <link>";
    private static final String REGISTER_MESSAGE = "Please, register with /start";
    private static final String NOT_TRACKED_MESSAGE = "The link is not being tracked";
    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION_OF_COMMAND = "Stop link tracking";

    @Override
    public String execute(Update update) {
        long userId = update.message().chat().id();
        String[] parts = update.message().text().split(" ");
        return untrackCommand(userId, parts);
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

    private String untrackCommand(Long id, String[] parts) {
        if (parts.length != 2) {
            return INVALID_FORMAT_MESSAGE;
        }
        String link = parts[1];
        User user = userRepository.findById(id);
        if (user == null) {
            return REGISTER_MESSAGE;
        }
        if (!user.getLinks().contains(link)) {
            return NOT_TRACKED_MESSAGE;
        }
        user.getLinks().remove(link);
        userRepository.updateUserById(id, user);
        return DESCRIPTION_OF_COMMAND;
    }

}
