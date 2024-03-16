package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.Link;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class LinkRowMapper implements RowMapper<Link> {
    @SneakyThrows
    @Override
    public Link mapRow(ResultSet rs, int rowNum) {
        return new Link(
            rs.getLong("id"),
            rs.getString("url"),
            rs.getTimestamp("last_updated_at").toLocalDateTime()
        );
    }
}
