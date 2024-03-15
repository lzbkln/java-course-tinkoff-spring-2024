package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.LinkageTable;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class LinkageTableRowMapper implements RowMapper<LinkageTable> {
    @SneakyThrows
    @Override
    public LinkageTable mapRow(ResultSet rs, int rowNum) {
        return new LinkageTable(
            rs.getLong("id"),
            rs.getLong("chat_id"),
            rs.getLong("link_id")
        );
    }
}
