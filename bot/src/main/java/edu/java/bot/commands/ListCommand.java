package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.entities.User;
import edu.java.bot.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/list")
@RequiredArgsConstructor
public class ListCommand implements Command {
    public final UserRepository userRepository;
    private static final String REGISTER_MESSAGE = "Please, register with /start";
    private static final String NO_LINKS_TRACKED_MESSAGE = "No links are being tracked.";
    private static final String TRACKED_LINKS_HEADER = "Tracked links:\n";
    private static final String COMMAND = "/list";
    private static final String DESCRIPTION_OF_COMMAND = "Show list of tracked links";

    @Override
    public String execute(Update update) {
        return getListTrackedLinks(update.message().chat().id());
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

    private String getListTrackedLinks(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return REGISTER_MESSAGE;
        }
        List<String> trackedLinks = user.getLinks();
        if (trackedLinks.isEmpty()) {
            return NO_LINKS_TRACKED_MESSAGE;
        } else {
            StringBuilder result = new StringBuilder(TRACKED_LINKS_HEADER);
            for (String link : trackedLinks) {
                result.append(link).append("\n");
            }
            return result.toString();
        }
    }
}
