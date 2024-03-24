package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.StackOverflowQuestion;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class StackOverflowQuestionRowMapper implements RowMapper<StackOverflowQuestion> {
    @SneakyThrows
    @Override
    public StackOverflowQuestion mapRow(ResultSet rs, int rowNum) {
        return new StackOverflowQuestion(
            rs.getLong("link_id"),
            rs.getInt("answer_count")
        );
    }
}
