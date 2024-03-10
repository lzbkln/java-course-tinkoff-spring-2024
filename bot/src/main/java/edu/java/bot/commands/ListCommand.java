package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.ScrapperLinksClient;
import edu.java.bot.dto.responses.LinkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component("/list")
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final ScrapperLinksClient scrapperLinksClient;
    private static final String EXCEPTION_MESSAGE = "You are not register";
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

    public String getListTrackedLinks(Long id) {
        return scrapperLinksClient.getAllLinks(id)
            .map(response -> {
                if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null && response.getBody().links() != null) {
                    if (response.getBody().links().isEmpty()) {
                        return NO_LINKS_TRACKED_MESSAGE;
                    } else {
                        StringBuilder result = new StringBuilder(TRACKED_LINKS_HEADER);
                        for (LinkResponse link : response.getBody().links()) {
                            result.append(link.url()).append("\n");
                        }
                        return result.toString();
                    }
                }
                return null;
            }).onErrorReturn(EXCEPTION_MESSAGE).block();
    }

}
