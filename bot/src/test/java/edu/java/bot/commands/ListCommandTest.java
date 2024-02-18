package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.entities.User;
import edu.java.bot.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {BotApplication.class})
public class ListCommandTest {
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private UserRepository userRepository;
    @Mock
    private User user = new User(1L);
    private ListCommand listCommand;

    @BeforeEach
    public void setUp() {
        listCommand = new ListCommand(userRepository);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
    }

    @Test
    @DisplayName("Test that /list command without registration returned valid message")
    public void testThatListCommandWithoutRegistrationReturnedValidMessage() {
        Mockito.doReturn(null).when(userRepository).findById(12L);
        assertEquals("Please, register with /start", listCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /list command returned valid message for blank list of links")
    public void testThatListCommandReturnedValidMessageForBlankListOfLinks() {
        Mockito.doReturn(new User(1L)).when(userRepository).findById(1L);
        assertEquals("No links are being tracked.", listCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /list command returned valid message for not blank list of links")
    public void testThatListCommandReturnedValidMessageForNotBlankListOfLinks() {
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(user);
        Mockito.when(user.getLinks()).thenReturn(List.of("https://github.com"));
        assertEquals("Tracked links:\nhttps://github.com\n", listCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /list command returned valid name and description")
    public void testThatHelpCommandReturnedValidNameAndDescription() {
        String expectedNameOfCommand = "/list";
        String expectedDescriptionOfCommand = "Show list of tracked links";

        assertEquals(listCommand.nameCommand(), expectedNameOfCommand);
        assertEquals(listCommand.descriptionOfCommand(), expectedDescriptionOfCommand);
    }

    @Test
    @DisplayName("Test getBotCommand returns correct BotCommand instance")
    public void testGetBotCommand() {
        BotCommand expectedBotCommand = new BotCommand("/list", "Show list of tracked links");
        assertEquals(expectedBotCommand.command(), listCommand.getBotCommand().command());
        assertEquals(expectedBotCommand.description(), listCommand.getBotCommand().description());
    }
}


