package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.Linkage;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class LinkageRowMapper implements RowMapper<Linkage> {
    @SneakyThrows
    @Override
    public Linkage mapRow(ResultSet rs, int rowNum) {
        return new Linkage(
            rs.getLong("chat_id"),
            rs.getLong("link_id")
        );
    }
}
