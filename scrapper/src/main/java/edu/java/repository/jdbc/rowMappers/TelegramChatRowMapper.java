package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.TelegramChat;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class TelegramChatRowMapper implements RowMapper<TelegramChat> {

    @Override
    @SneakyThrows
    public TelegramChat mapRow(ResultSet rs, int rowNum) {
        return new TelegramChat(
            rs.getLong("chat_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
