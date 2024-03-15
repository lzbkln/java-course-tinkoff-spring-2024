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

    @Override
    @Transactional
    public void save(LinkageTable chatLink) {
        jdbcClient.sql("INSERT INTO linkage_table (chat_id, link_id) VALUES (:chat_id, :link_id)")
            .param("chat_id", chatLink.getChatId())
            .param("link_id", chatLink.getLinkId())
            .update();
    }

    @Override
    @Transactional
    public List<LinkageTable> findByChatId(long chatId) {
        return jdbcClient.sql("SELECT * FROM linkage_table WHERE chat_id = :chat_id")
            .param("chat_id", chatId)
            .query(ROW_MAPPER)
            .list();
    }

    @Override
    @Transactional
    public List<LinkageTable> findByLinkId(long linkId) {
        return jdbcClient.sql("SELECT * FROM linkage_table WHERE link_id = :link_id")
            .param("link_id", linkId)
            .query(ROW_MAPPER)
            .list();
    }

    @Override
    @Transactional
    public void removeByChatIdAndLinkId(long chatId, long linkId) {
        jdbcClient.sql("DELETE FROM linkage_table WHERE chat_id = :chat_id AND link_id = :link_id")
            .param("chat_id", chatId)
            .param("link_id", linkId)
            .update();
    }
}
