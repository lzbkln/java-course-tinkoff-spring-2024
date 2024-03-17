package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.TelegramChat;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class TelegramChatRowMapper implements RowMapper<TelegramChat> {

    @Override
    @SneakyThrows
    public TelegramChat mapRow(ResultSet rs, int rowNum) {
        return new TelegramChat(
            rs.getLong("id"),
            rs.getObject("created_at", OffsetDateTime.class)
        );
    }
}
