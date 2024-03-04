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
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public class TrackCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private ScrapperLinksClient scrapperLinksClient;
    private TrackCommand trackCommand;
    @Autowired
    private LinkHandler linkHandler;

    @BeforeEach
    public void setUp() {
        trackCommand = new TrackCommand(scrapperLinksClient, linkHandler);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);
    }

    @Test
    @DisplayName("Test that invalid /track command returned valid message")
    public void testThatInvalidTrackCommandReturnedValidMessage() throws URISyntaxException {
        Mockito.doReturn("/track").when(message).text();

        assertEquals("Invalid format. Please use: /track <link>", trackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /track with invalid link returned valid message")
    public void testThatTrackCommandWithInvalidLinkReturnedValidMessage() throws URISyntaxException {
        Mockito.doReturn("/track hps://github.com").when(message).text();

        assertEquals("The link is not valid", trackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /track with valid link returned valid message")
    public void testThatTrackCommandWithValidLinkReturnedValidMessage() throws URISyntaxException {
        Mockito.doReturn("/track https://github.com").when(message).text();
        Mockito.doReturn(Mono.just(ResponseEntity.ok().build()))
            .when(scrapperLinksClient).addLink(Mockito.any(), Mockito.any());

        assertEquals("Start link tracking", trackCommand.execute(update));
    }

    @Test
    @DisplayName("Test getBotCommand returns correct BotCommand instance")
    public void testGetBotCommand() {
        BotCommand expectedBotCommand = new BotCommand("/track", "Start link tracking");
        assertEquals(expectedBotCommand.command(), trackCommand.getBotCommand().command());
        assertEquals(expectedBotCommand.description(), trackCommand.getBotCommand().description());
    }

    @Test
    @DisplayName("Test that /track command returned valid name and description")
    public void testThatHelpCommandReturnedValidNameAndDescription() {
        String expectedNameOfCommand = "/track";
        String expectedDescriptionOfCommand = "Start link tracking";

        assertEquals(trackCommand.nameCommand(), expectedNameOfCommand);
        assertEquals(trackCommand.descriptionOfCommand(), expectedDescriptionOfCommand);
    }

}
