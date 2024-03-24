package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.clients.ScrapperLinksClient;
import edu.java.bot.handlers.LinkHandler;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {BotApplication.class})
public class UntrackCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private ScrapperLinksClient scrapperLinksClient;
    private UntrackCommand untrackCommand;
    @Autowired
    private LinkHandler linkHandler;

    @BeforeEach
    public void setUp() {
        untrackCommand = new UntrackCommand(scrapperLinksClient, linkHandler);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
    }

    @Test
    @DisplayName("Test that invalid /untrack command returned valid message")
    public void testThatInvalidTrackCommandReturnedValidMessage() throws URISyntaxException {
        Mockito.doReturn("/untrack").when(message).text();

        assertEquals("Invalid format. Please use: /untrack <link>", untrackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /untrack with invalid link returned valid message")
    public void testThatTrackCommandWithInvalidLinkReturnedValidMessage() throws URISyntaxException {
        Mockito.doReturn("/untrack hts://github.com").when(message).text();

        assertEquals("The link is not valid", untrackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /untrack with valid link returned valid message")
    public void testThatTrackCommandWithValidLinkReturnedValidMessage() throws URISyntaxException {
        Mockito.doReturn("/untrack https://github.com/lzbkln/java-course-tinkoff-spring-2024").when(message).text();
        Mockito.doReturn(Mono.just(ResponseEntity.ok().build()))
            .when(scrapperLinksClient).deleteLink(Mockito.any(), Mockito.any());

        assertEquals("Stop link tracking", untrackCommand.execute(update));
    }

    @Test
    @DisplayName("Test getBotCommand returns correct BotCommand instance")
    public void testGetBotCommand() {
        BotCommand expectedBotCommand = new BotCommand("/untrack", "Stop link tracking");
        assertEquals(expectedBotCommand.command(), untrackCommand.getBotCommand().command());
        assertEquals(expectedBotCommand.description(), untrackCommand.getBotCommand().description());
    }

    @Test
    @DisplayName("Test that /untrack command returned valid name and description")
    public void testThatHelpCommandReturnedValidNameAndDescription() {
        String expectedNameOfCommand = "/untrack";
        String expectedDescriptionOfCommand = "Stop link tracking";

        assertEquals(untrackCommand.nameCommand(), expectedNameOfCommand);
        assertEquals(untrackCommand.descriptionOfCommand(), expectedDescriptionOfCommand);
    }

}
