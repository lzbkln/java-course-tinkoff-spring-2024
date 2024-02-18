package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.entities.User;
import edu.java.bot.handlers.LinkHandler;
import edu.java.bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/untrack")
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final UserRepository userRepository;
    private final LinkHandler linkHandler;
    private static final String INVALID_FORMAT_MESSAGE = "Invalid format. Please use: /untrack <link>";
    private static final String REGISTER_MESSAGE = "Please, register with /start";
    private static final String NOT_TRACKED_MESSAGE = "The link is not being tracked";
    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION_OF_COMMAND = "Stop link tracking";
    private static final String NOT_VALID_LINK = "The link is not valid";

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
        User user = userRepository.findById(id);
        if (user == null) {
            return REGISTER_MESSAGE;
        }

        if (parts.length != 2) {
            return INVALID_FORMAT_MESSAGE;
        }

        String link = parts[1];
        return processLink(user, link);
    }

    private String processLink(User user, String link) {
        if (!linkHandler.isValid(link)) {
            return NOT_VALID_LINK;
        }

        if (!user.getLinks().contains(link)) {
            return NOT_TRACKED_MESSAGE;
        }

        removeLinkAndUpdateUser(user, link);
        return DESCRIPTION_OF_COMMAND;
    }

    private void removeLinkAndUpdateUser(User user, String link) {
        user.getLinks().remove(link);
        userRepository.updateUserById(user.getId(), user);
    }
}
