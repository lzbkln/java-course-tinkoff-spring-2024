package edu.java.repository.jdbc;

import edu.java.repository.LinkRepository;
import edu.java.repository.entity.Link;
import edu.java.repository.jdbc.rowMappers.LinkRowMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcClient jdbcClient;
    private static final RowMapper<Link> ROW_MAPPER = new LinkRowMapper();

    @Override
    @Transactional
    public void save(Link link) {
        jdbcClient.sql("INSERT INTO links (url, last_updated_at) VALUES (:url, :last_updated_at)")
            .param("url", link.getUrl())
            .param("last_updated_at", link.getLastUpdatedAt())
            .update();
    }

    @Override
    @Transactional
    public Optional<Link> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM links WHERE link_id = :id")
            .param("id", id)
            .query(ROW_MAPPER)
            .optional();
    }

    @Override
    @Transactional
    public Optional<Link> findByUrl(String url) {
        return jdbcClient.sql("SELECT * FROM links WHERE url = :url")
            .param("url", url)
            .query(ROW_MAPPER)
            .optional();
    }

    @Override
    @Transactional
    public boolean removeById(Long id) {
        int rowsAffected = jdbcClient.sql("DELETE FROM links WHERE link_id = :id")
            .param("id", id)
            .update();
        return rowsAffected > 0;
    }
}
