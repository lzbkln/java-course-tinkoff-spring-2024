package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpCommandTest {
    HelpCommand helpCommand;

    @BeforeEach
    public void setUp() {
        helpCommand = new HelpCommand();
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
            /help - Get list of all commands""";

        Update mockUpdate = Mockito.mock(Update.class);

        String actualResponse = helpCommand.execute(mockUpdate);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Test that /help command returned valid name and description")
    public void testThatHelpCommandReturnedValidNameAndDescription() {
        String expectedNameOfCommand = "/help";
        String expectedDescriptionOfCommand = "Get list of all commands";

        assertEquals(helpCommand.nameCommand(), expectedNameOfCommand);
        assertEquals(helpCommand.descriptionOfCommand(), expectedDescriptionOfCommand);
    }
}
