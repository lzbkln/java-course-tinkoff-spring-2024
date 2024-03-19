package edu.java.scrapper.repository.jdbc;

import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.TelegramChat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JdbcTelegramChatRepositoryTest extends IntegrationTest {
    @Autowired
    TelegramChatRepository telegramChatRepository;
    private static final Long ID = 123L;

    @Test
    @DisplayName("Test that save user persists user")
    void testThatSaveUserPersistsUser() {
        TelegramChat telegramChat = new TelegramChat(ID);

        telegramChatRepository.saveUser(telegramChat);

        assertThat(telegramChatRepository.findById(ID)).isNotNull();
    }

    @Test
    @DisplayName("Test that find by id returned correct user")
    void testThatFindByIdReturnedCorrectUser() {
        TelegramChat telegramChat = new TelegramChat(ID);
        telegramChatRepository.saveUser(telegramChat);

        TelegramChat foundTelegramChat = telegramChatRepository.findById(ID);

        assertThat(foundTelegramChat.getId()).isEqualTo(telegramChat.getId());
    }

    @Test
    @DisplayName("Test that delete by id removes user")
    void testThatDeleteByIdRemovesUser() {
        TelegramChat telegramChat = new TelegramChat(ID);
        telegramChatRepository.saveUser(telegramChat);

        assertThat(telegramChatRepository.findById(ID)).isNotNull();
        telegramChatRepository.deleteById(ID);
        assertThrows(EmptyResultDataAccessException.class, () -> telegramChatRepository.findById(ID));
    }
}
