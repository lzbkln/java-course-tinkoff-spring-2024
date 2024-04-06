package edu.java.scrapper.service.jooq;

import edu.java.ScrapperApplication;
import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.TelegramChat;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.TelegramChatService;
import edu.java.service.exceptions.ReRegistrationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JooqTelegramChatServiceTest extends IntegrationTest {
    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JOOQ");
    }

    @Autowired
    private TelegramChatService jooqTelegramChatService;

    @Autowired
    private TelegramChatRepository jooqTelegramChatRepository;
    private static final Long ID = 1L;

    @Test
    @DisplayName("Test that method throws ReRegistrationException when DuplicateKeyException is thrown")
    void testThatRegisterMethodThrowsReRegistrationExceptionWhenDuplicateKeyExceptionIsThrown() {
        jooqTelegramChatService.register(ID);

        assertThrows(ReRegistrationException.class, () -> jooqTelegramChatService.register(ID));
    }

    @Test
    @DisplayName("Test that method successfully registers chat")
    void testThatMethodSuccessfullyRegistersChat() {
        jooqTelegramChatService.register(ID);

        TelegramChat registeredChat = jooqTelegramChatRepository.findById(ID);
        assertNotNull(registeredChat);
        assertEquals(ID, registeredChat.getId());
    }

    @Test
    @DisplayName("Test that method successfully unregisters chat")
    void testThatMethodSuccessfullyUnregistersChat() {
        jooqTelegramChatRepository.saveUser(new TelegramChat(ID));

        jooqTelegramChatService.unregister(ID);

        assertNull(jooqTelegramChatRepository.findById(ID));
    }
}
