package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.entities.User;
import edu.java.bot.handlers.LinkHandler;
import edu.java.bot.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {BotApplication.class})
public class TrackCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private UserRepository userRepository;
    private TrackCommand trackCommand;
    @Autowired
    private LinkHandler linkHandler;
    @Mock
    private User user = new User(1L);

    @BeforeEach
    public void setUp() {
        trackCommand = new TrackCommand(userRepository, linkHandler);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
    }

    @Test
    @DisplayName("Test that /list command without registration returned valid message")
    public void testThatListCommandWithoutRegistrationReturnedValidMessage() {
        Mockito.doReturn("/track https://github.com").when(message).text();
        Mockito.doReturn(null).when(userRepository).findById(12L);
        assertEquals("Please, register with /start", trackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that invalid /track command returned valid message")
    public void testThatInvalidTrackCommandReturnedValidMessage() {
        Mockito.doReturn(new User(1L)).when(userRepository).findById(1L);
        Mockito.doReturn("/track").when(message).text();
        assertEquals("Invalid format. Please use: /track <link>", trackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /track with invalid link returned valid message")
    public void testThatTrackCommandWithInvalidLinkReturnedValidMessage() {
        Mockito.doReturn(new User(1L)).when(userRepository).findById(1L);
        Mockito.doReturn("/track htps://github.com").when(message).text();
        assertEquals("The link is not valid", trackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /track with valid link returned valid message")
    public void testThatTrackCommandWithValidLinkReturnedValidMessage() {
        Mockito.doReturn(new User(1L)).when(userRepository).findById(1L);
        Mockito.doReturn("/track https://github.com").when(message).text();
        assertEquals("Start link tracking", trackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /track with tracked link returned valid message")
    public void testThatTrackCommandWithTrackedLinkReturnedValidMessage() {
        Mockito.doReturn("/track https://github.com").when(message).text();
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(user);
        Mockito.when(user.getLinks()).thenReturn(List.of("https://github.com"));
        assertEquals("The link is being tracked", trackCommand.execute(update));
    }

    @Test
    @DisplayName("Test getBotCommand returns correct BotCommand instance")
    public void testGetBotCommand() {
        BotCommand expectedBotCommand = new BotCommand("/track", "Start link tracking");
        assertEquals(expectedBotCommand.command(), trackCommand.getBotCommand().command());
        assertEquals(expectedBotCommand.description(), trackCommand.getBotCommand().description());
    }

    @Test
    @DisplayName("Test that /help command returned valid name and description")
    public void testThatHelpCommandReturnedValidNameAndDescription() {
        String expectedNameOfCommand = "/track";
        String expectedDescriptionOfCommand = "Start link tracking";

        assertEquals(trackCommand.nameCommand(), expectedNameOfCommand);
        assertEquals(trackCommand.descriptionOfCommand(), expectedDescriptionOfCommand);
    }

}
