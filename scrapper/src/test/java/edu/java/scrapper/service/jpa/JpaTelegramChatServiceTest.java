package edu.java.scrapper.service.jpa;

import edu.java.ScrapperApplication;
import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.repository.jpa.entity.JpaTelegramChat;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.TelegramChatService;
import edu.java.service.exceptions.NonRegisterChatException;
import edu.java.service.exceptions.ReRegistrationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
@DirtiesContext
public class JpaTelegramChatServiceTest extends IntegrationTest {
    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JPA");
    }

    @Autowired
    private TelegramChatService jpaTelegramChatService;

    @Autowired
    private JpaTelegramChatRepository jpaTelegramChatRepository;
    private static final Long ID = 1L;

    @Test
    @DisplayName("Test that method throws ReRegistrationException")
    void testThatRegisterMethodThrowsReRegistrationException() {
        jpaTelegramChatService.register(ID);

        assertThrows(ReRegistrationException.class, () -> jpaTelegramChatService.register(ID));
    }

    @Test
    @DisplayName("Test that method throws NonRegisterChatException")
    void testThatRegisterMethodThrowsNonExistingChatWhenDuplicateKeyExceptionIsThrown() {

        assertThrows(NonRegisterChatException.class, () -> jpaTelegramChatService.unregister(ID));
    }

    @Test
    @DisplayName("Test that method successfully registers chat")
    void testThatMethodSuccessfullyRegistersChat() {
        jpaTelegramChatService.register(ID);

        JpaTelegramChat registeredChat = jpaTelegramChatRepository.findById(ID).get();
        assertNotNull(registeredChat);
        assertEquals(ID, registeredChat.getId());
    }

    @Test
    @DisplayName("Test that method successfully unregisters chat")
    void testThatMethodSuccessfullyUnregistersChat() {
        jpaTelegramChatRepository.saveAndFlush(new JpaTelegramChat(ID));

        jpaTelegramChatService.unregister(ID);

        assertTrue(jpaTelegramChatRepository.findById(ID).isEmpty());
    }
}
