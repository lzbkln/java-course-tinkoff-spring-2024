package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.Link;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class LinkRowMapper implements RowMapper<Link> {
    @SneakyThrows
    @Override
    public Link mapRow(ResultSet rs, int rowNum) {
        return new Link(
            rs.getLong("id"),
            rs.getString("url"),
            rs.getObject("last_updated_at", OffsetDateTime.class)
        );
    }
}
