package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.clients.ScrapperChatClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {BotApplication.class})
@DirtiesContext
public class StartCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private ScrapperChatClient scrapperChatClient;
    private StartCommand startCommand;

    @BeforeEach
    public void setUp() {
        startCommand = new StartCommand(scrapperChatClient);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
    }

    @Test
    @DisplayName("Test that /start command returned valid message")
    public void testThatStartCommandReturnedValidMessage() {
        Mockito.doReturn(Mono.just(ResponseEntity.ok().build())).when(scrapperChatClient).registerChat(1L);
        String expectedResponse = "Successful registration";
        assertEquals(expectedResponse, startCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /start command returned valid name and description")
    public void testThatHelpCommandReturnedValidNameAndDescription() {
        String expectedNameOfCommand = "/start";
        String expectedDescriptionOfCommand = "Register user";

        assertEquals(startCommand.nameCommand(), expectedNameOfCommand);
        assertEquals(startCommand.descriptionOfCommand(), expectedDescriptionOfCommand);
    }

    @Test
    @DisplayName("Test getBotCommand returns correct BotCommand instance")
    public void testGetBotCommand() {
        BotCommand expectedBotCommand = new BotCommand("/start", "Register user");
        assertEquals(expectedBotCommand.command(), startCommand.getBotCommand().command());
        assertEquals(expectedBotCommand.description(), startCommand.getBotCommand().description());
    }
}
