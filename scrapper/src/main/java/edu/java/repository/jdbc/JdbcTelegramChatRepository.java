package edu.java.repository.jdbc;

import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.TelegramChat;
import edu.java.repository.jdbc.rowMappers.TelegramChatRowMapper;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Getter
@RequiredArgsConstructor
public class JdbcTelegramChatRepository implements TelegramChatRepository {
    private final JdbcClient jdbcClient;
    private static final RowMapper<TelegramChat> ROW_MAPPER = new TelegramChatRowMapper();

    @Override
    @Transactional
    public void saveUser(TelegramChat user) {
        jdbcClient.sql("INSERT INTO chats (chat_id) VALUES (:chat_id)")
            .param("chat_id", user.getChatId())
            .update();
    }

    @Override
    public Optional<TelegramChat> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM chats WHERE chat_id = :chat_id")
            .param("chat_id", id)
            .query(ROW_MAPPER)
            .optional();
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        int rowsAffected = jdbcClient.sql("DELETE FROM chats WHERE chat_id = :chat_id")
            .param("chat_id", id)
            .update();
        return rowsAffected > 0;
    }
}
