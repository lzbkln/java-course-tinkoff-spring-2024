package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {BotApplication.class})
@DirtiesContext
public class HelpCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @InjectMocks
    private HelpCommand helpCommand;
    @Mock
    private Command startCommand;
    @Mock
    private Command listCommand;
    @Mock
    private Command trackCommand;
    @Mock
    private Command untrackCommand;

    @BeforeEach
    public void setUp() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);

        Mockito.when(startCommand.nameCommand()).thenReturn("/start");
        Mockito.when(startCommand.descriptionOfCommand()).thenReturn("Register user");

        Mockito.when(listCommand.nameCommand()).thenReturn("/list");
        Mockito.when(listCommand.descriptionOfCommand()).thenReturn("Show list of tracked links");

        Mockito.when(trackCommand.nameCommand()).thenReturn("/track");
        Mockito.when(trackCommand.descriptionOfCommand()).thenReturn("Start link tracking");

        Mockito.when(untrackCommand.nameCommand()).thenReturn("/untrack");
        Mockito.when(untrackCommand.descriptionOfCommand()).thenReturn("Stop link tracking");
    }

    @Test
    @DisplayName("Test that /help command returned valid message")
    public void testThatHelpCommandReturnedValidMessage() {
        String expectedResponse = """
            This bot works as a single update tracking method for many resources.
            First you need to register:
            /start - Register user

            All commands:
            /track - Start link tracking

            /untrack - Stop link tracking

            /list - Show list of tracked links

            /help - Get list of all commands
            """;

        assertEquals(expectedResponse, helpCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /help command returned valid name and description")
    public void testThatHelpCommandReturnedValidNameAndDescription() {
        String expectedNameOfCommand = "/help";
        String expectedDescriptionOfCommand = "Get list of all commands";

        assertEquals(helpCommand.nameCommand(), expectedNameOfCommand);
        assertEquals(helpCommand.descriptionOfCommand(), expectedDescriptionOfCommand);
    }

    @Test
    @DisplayName("Test getBotCommand returns correct BotCommand instance")
    public void testGetBotCommand() {
        BotCommand expectedBotCommand = new BotCommand("/help", "Get list of all commands");
        assertEquals(expectedBotCommand.command(), helpCommand.getBotCommand().command());
        assertEquals(expectedBotCommand.description(), helpCommand.getBotCommand().description());
    }
}
