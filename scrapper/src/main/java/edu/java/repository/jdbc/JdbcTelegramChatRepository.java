package edu.java.repository.jdbc;

import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.TelegramChat;
import edu.java.repository.jdbc.rowMappers.TelegramChatRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcTelegramChatRepository implements TelegramChatRepository {
    private final JdbcClient jdbcClient;
    private static final RowMapper<TelegramChat> ROW_MAPPER = new TelegramChatRowMapper();
    private static final String INSERT_SQL = "INSERT INTO chats (id) VALUES (:id)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM chats WHERE id = :id";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM chats WHERE id = :id";
    private static final String CHAT_ID = "id";

    @Override
    @Transactional
    public void saveUser(TelegramChat user) {
        jdbcClient.sql(INSERT_SQL)
            .param(CHAT_ID, user.getId())
            .update();
    }

    @Override
    public TelegramChat findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
            .param(CHAT_ID, id)
            .query(ROW_MAPPER)
            .single();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jdbcClient.sql(DELETE_BY_ID_SQL)
            .param(CHAT_ID, id)
            .update();
    }
}
