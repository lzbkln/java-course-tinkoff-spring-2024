package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.entities.User;
import edu.java.bot.handlers.LinkHandler;
import edu.java.bot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
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
    private UserRepository userRepository;
    private UntrackCommand untrackCommand;
    @Autowired
    private LinkHandler linkHandler;
    @Mock
    private User user = new User(1L);

    @BeforeEach
    public void setUp() {
        untrackCommand = new UntrackCommand(userRepository, linkHandler);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
    }

    @Test
    @DisplayName("Test that /untrack command without registration returned valid message")
    public void testThatListCommandWithoutRegistrationReturnedValidMessage() {
        Mockito.doReturn("/untrack https://github.com").when(message).text();
        Mockito.doReturn(null).when(userRepository).findById(12L);
        assertEquals("Please, register with /start", untrackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that invalid /untrack command returned valid message")
    public void testThatInvalidTrackCommandReturnedValidMessage() {
        Mockito.doReturn(new User(1L)).when(userRepository).findById(1L);
        Mockito.doReturn("/untrack").when(message).text();
        assertEquals("Invalid format. Please use: /untrack <link>", untrackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /untrack with invalid link returned valid message")
    public void testThatTrackCommandWithInvalidLinkReturnedValidMessage() {
        Mockito.doReturn(new User(1L)).when(userRepository).findById(1L);
        Mockito.doReturn("/untrack htps://github.com").when(message).text();
        assertEquals("The link is not valid", untrackCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /untrack with not tracked link returned valid message")
    public void testThatTrackCommandWithTrackedLinkReturnedValidMessage() {
        Mockito.doReturn("/untrack https://github.com").when(message).text();
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(user);
        Mockito.when(user.getLinks()).thenReturn(List.of());
        assertEquals("The link is not being tracked", untrackCommand.execute(update));
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
