package edu.java.repository.jdbc;

import edu.java.repository.LinkageRepository;
import edu.java.repository.entity.Linkage;
import edu.java.repository.jdbc.rowMappers.LinkageRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcLinkageRepository implements LinkageRepository {
    private final JdbcClient jdbcClient;
    private static final RowMapper<Linkage> ROW_MAPPER = new LinkageRowMapper();
    private static final String INSERT_SQL =
        "INSERT INTO linkage (chat_id, link_id) VALUES (:chat_id, :link_id)";
    private static final String FIND_BY_CHAT_ID_SQL =
        "SELECT * FROM linkage WHERE chat_id = :chat_id";
    private static final String FIND_BY_LINK_ID_SQL =
        "SELECT * FROM linkage WHERE link_id = :link_id";
    private static final String FIND_BY_LINK_ID_AND_CHAT_ID_SQL =
        "SELECT * FROM linkage WHERE link_id = :link_id AND chat_id = :chat_id";
    private static final String DELETE_SQL =
        "DELETE FROM linkage WHERE chat_id = :chat_id AND link_id = :link_id";
    private static final String COUNT_BY_LINK_ID_SQL =
        "SELECT COUNT(*) FROM linkage WHERE link_id = :link_id";
    private static final String LINK_ID = "link_id";
    private static final String CHAT_ID = "chat_id";

    @Override
    public void save(Linkage linkage) {
        jdbcClient.sql(INSERT_SQL)
            .param(CHAT_ID, linkage.getChatId())
            .param(LINK_ID, linkage.getLinkId())
            .update();
    }

    @Override
    public List<Linkage> findByChatId(Long chatId) {
        return jdbcClient.sql(FIND_BY_CHAT_ID_SQL)
            .param(CHAT_ID, chatId)
            .query(ROW_MAPPER)
            .list();
    }

    @Override
    public List<Linkage> findByLinkId(Long linkId) {
        return jdbcClient.sql(FIND_BY_LINK_ID_SQL)
            .param(LINK_ID, linkId)
            .query(ROW_MAPPER)
            .list();
    }

    @Override
    public void removeByChatIdAndLinkId(Long chatId, Long linkId) {
        jdbcClient.sql(DELETE_SQL)
            .param(CHAT_ID, chatId)
            .param(LINK_ID, linkId)
            .update();
    }

    @Override
    public Integer countByLinkId(Long linkId) {
        return jdbcClient.sql(COUNT_BY_LINK_ID_SQL)
            .param(LINK_ID, linkId)
            .query(Integer.class).single();
    }

    @Override
    public boolean findByLinkIdAndChatId(Long linkId, Long chatId) {
        Optional<Linkage> linkage = jdbcClient.sql(FIND_BY_LINK_ID_AND_CHAT_ID_SQL)
            .param(CHAT_ID, chatId)
            .param(LINK_ID, linkId)
            .query(ROW_MAPPER)
            .optional();
        return linkage.isPresent();
    }
}
