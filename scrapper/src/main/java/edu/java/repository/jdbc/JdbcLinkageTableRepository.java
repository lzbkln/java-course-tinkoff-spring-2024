package edu.java.repository.jdbc;

import edu.java.repository.LinkageTableRepository;
import edu.java.repository.entity.LinkageTable;
import edu.java.repository.jdbc.rowMappers.LinkageTableRowMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkageTableRepository implements LinkageTableRepository {
    private final JdbcClient jdbcClient;
    private static final RowMapper<LinkageTable> ROW_MAPPER = new LinkageTableRowMapper();
    private static final String INSERT_SQL =
        "INSERT INTO linkage_table (chat_id, link_id) VALUES (:chat_id, :link_id)";
    private static final String FIND_BY_CHAT_ID_SQL =
        "SELECT * FROM linkage_table WHERE chat_id = :chat_id";
    private static final String FIND_BY_LINK_ID_SQL =
        "SELECT * FROM linkage_table WHERE link_id = :link_id";
    private static final String DELETE_SQL =
        "DELETE FROM linkage_table WHERE chat_id = :chat_id AND link_id = :link_id";
    private static final String COUNT_BY_LINK_ID_SQL =
        "SELECT COUNT(*) FROM linkage_table WHERE link_id = :link_id";
    private static final String LINK_ID = "link_id";
    private static final String CHAT_ID = "chat_id";

    @Override
    @Transactional
    public void save(LinkageTable chatLink) {
        jdbcClient.sql(INSERT_SQL)
            .param(CHAT_ID, chatLink.getChatId())
            .param(LINK_ID, chatLink.getLinkId())
            .update();
    }

    @Override
    @Transactional
    public List<LinkageTable> findByChatId(Long chatId) {
        return jdbcClient.sql(FIND_BY_CHAT_ID_SQL)
            .param(CHAT_ID, chatId)
            .query(ROW_MAPPER)
            .list();
    }

    @Override
    @Transactional
    public List<LinkageTable> findByLinkId(Long linkId) {
        return jdbcClient.sql(FIND_BY_LINK_ID_SQL)
            .param(LINK_ID, linkId)
            .query(ROW_MAPPER)
            .list();
    }

    @Override
    @Transactional
    public void removeByChatIdAndLinkId(Long chatId, Long linkId) {
        jdbcClient.sql(DELETE_SQL)
            .param(CHAT_ID, chatId)
            .param(LINK_ID, linkId)
            .update();
    }

    @Override
    @Transactional
    public Integer countByLinkId(Long linkId) {
        return jdbcClient.sql(COUNT_BY_LINK_ID_SQL)
            .param(LINK_ID, linkId)
            .query(Integer.class).single();
    }
}
