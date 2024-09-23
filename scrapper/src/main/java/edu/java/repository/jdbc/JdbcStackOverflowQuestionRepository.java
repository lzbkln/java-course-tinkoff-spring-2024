package edu.java.repository.jdbc;

import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.entity.StackOverflowQuestion;
import edu.java.repository.jdbc.rowMappers.StackOverflowQuestionRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcStackOverflowQuestionRepository implements StackOverflowQuestionRepository {
    private final JdbcClient jdbcClient;
    private static final RowMapper<StackOverflowQuestion> ROW_MAPPER = new StackOverflowQuestionRowMapper();
    private static final String INSERT_SQL =
        "INSERT INTO stackoverflow_question (link_id, answer_count) VALUES (:link_id, :answer_count)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM stackoverflow_question WHERE link_id = :link_id";
    private static final String UPDATE_LINK_SQL =
        "UPDATE stackoverflow_question SET answer_count = :answer_count WHERE link_id = :link_id";
    private static final String REMOVE_BY_ID_SQL = "DELETE FROM stackoverflow_question WHERE link_id = :link_id";

    private static final String LINK_ID = "link_id";
    private static final String COUNT = "answer_count";

    @Override
    public void save(StackOverflowQuestion stackOverflowQuestion) {
        jdbcClient.sql(INSERT_SQL)
            .param(LINK_ID, stackOverflowQuestion.getLinkId())
            .param(COUNT, stackOverflowQuestion.getAnswerCount())
            .update();
    }

    @Override
    public StackOverflowQuestion getByLinkId(Long linkId) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
            .param(LINK_ID, linkId)
            .query(ROW_MAPPER)
            .single();
    }

    @Override
    public void updateData(StackOverflowQuestion stackOverflowQuestion) {
        jdbcClient.sql(UPDATE_LINK_SQL)
            .param(LINK_ID, stackOverflowQuestion.getLinkId())
            .param(COUNT, stackOverflowQuestion.getAnswerCount())
            .update();
    }

    @Override
    public void removeByLinkId(Long linkId) {
        jdbcClient.sql(REMOVE_BY_ID_SQL)
            .param(LINK_ID, linkId)
            .update();
    }
}
