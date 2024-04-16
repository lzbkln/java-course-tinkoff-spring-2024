package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.clients.ScrapperLinksClient;
import edu.java.bot.dto.responses.LinkResponse;
import edu.java.bot.dto.responses.ListLinksResponse;
import java.net.URI;
import java.util.List;
import org.apache.kafka.clients.admin.AdminClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {BotApplication.class})
@DirtiesContext
public class ListCommandTest {
    @MockBean AdminClient adminClient;
    @MockBean KafkaAdmin kafkaAdmin;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private ScrapperLinksClient scrapperLinksClient;
    private ListCommand listCommand;

    @BeforeEach
    public void setUp() {
        listCommand = new ListCommand(scrapperLinksClient);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
    }

    @Test
    @DisplayName("Test that /list command returned valid message for blank list of links")
    public void testThatListCommandReturnedValidMessageForBlankListOfLinks() {
        Mockito.doReturn(Mono.just(ResponseEntity.ok().body(new ListLinksResponse(List.of(), 0))))
            .when(scrapperLinksClient).getAllLinks(1L);

        assertEquals("No links are being tracked.", listCommand.execute(update));
    }

    @Test
    @DisplayName("Test that /list command returned valid message for not blank list of links")
    public void testThatListCommandReturnedValidMessageForNotBlankListOfLinks() {
        Mockito.doReturn(Mono.just(ResponseEntity.ok()
                .body(new ListLinksResponse(
                    List.of(new LinkResponse(0L, URI.create("https://github.com"))),
                    1
                ))))
            .when(scrapperLinksClient).getAllLinks(1L);

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
