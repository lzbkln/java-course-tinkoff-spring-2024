package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.ScrapperLinksClient;
import edu.java.bot.dto.requests.RemoveLinkRequest;
import edu.java.bot.handlers.LinkHandler;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component("/untrack")
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final ScrapperLinksClient scrapperLinksClient;
    private final LinkHandler linkHandler;
    private static final String INVALID_FORMAT_MESSAGE = "Invalid format. Please use: /untrack <link>";
    private static final String EXCEPTION_MESSAGE = "The link is not being tracked or you are not register";
    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION_OF_COMMAND = "Stop link tracking";
    private static final String NOT_VALID_LINK = "The link is not valid";

    @Override
    public String execute(Update update) throws URISyntaxException {
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

    private String untrackCommand(Long id, String[] parts) throws URISyntaxException {

        if (parts.length != 2) {
            return INVALID_FORMAT_MESSAGE;
        }

        String link = parts[1];

        if (!linkHandler.isValid(link)) {
            return NOT_VALID_LINK;
        }

        return scrapperLinksClient.deleteLink(id, new RemoveLinkRequest(new URI(link)))
            .map(response -> {
                if (HttpStatus.OK.equals(response.getStatusCode())) {
                    return DESCRIPTION_OF_COMMAND;
                }
                return EXCEPTION_MESSAGE;
            }).onErrorReturn(EXCEPTION_MESSAGE).block();
    }
}
