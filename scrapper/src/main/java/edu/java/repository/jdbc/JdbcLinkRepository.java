package edu.java.repository.jdbc;

import edu.java.repository.LinkRepository;
import edu.java.repository.entity.Link;
import edu.java.repository.jdbc.rowMappers.LinkRowMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcClient jdbcClient;
    private static final RowMapper<Link> ROW_MAPPER = new LinkRowMapper();
    private static final String INSERT_SQL = "INSERT INTO links (url, last_updated_at) VALUES (:url, :last_updated_at)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM links WHERE id = :id";
    private static final String FIND_BY_URL_SQL = "SELECT * FROM links WHERE url = :url";
    private static final String REMOVE_BY_ID_SQL = "DELETE FROM links WHERE id = :id";
    private static final String UPDATE_LINK_SQL =
        "UPDATE links SET last_updated_at = :last_updated_at WHERE id = :id";
    private static final String FIND_LINKS_TO_UPDATE_SQL =
        "SELECT * FROM links WHERE last_updated_at < NOW() - INTERVAL '1 hour'";
    private static final String COUNT_BY_LINK_ID_SQL =
        "SELECT COUNT(*) FROM links WHERE url = :url";
    private static final String ID = "id";
    private static final String URL = "url";
    private static final String LAST_UPDATED_AT = "last_updated_at";

    @Override
    public void save(Link link) {
        jdbcClient.sql(INSERT_SQL)
            .param(URL, link.getUrl())
            .param(LAST_UPDATED_AT, link.getLastUpdatedAt())
            .update();
    }

    @Override
    public Link findById(Long id) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
            .param(ID, id)
            .query(ROW_MAPPER)
            .single();
    }

    @Override
    public Link findByUrl(String url) {
        return jdbcClient.sql(FIND_BY_URL_SQL)
            .param(URL, url)
            .query(ROW_MAPPER)
            .single();
    }

    @Override
    public boolean findByUrlBool(String url) {
        int count = jdbcClient.sql(COUNT_BY_LINK_ID_SQL)
            .param(URL, url)
            .query(Integer.class).single();
        return count > 0;
    }

    @Override
    public void removeById(Long id) {
        jdbcClient.sql(REMOVE_BY_ID_SQL)
            .param(ID, id)
            .update();
    }

    @Override
    public void updateLink(Link link) {
        jdbcClient.sql(UPDATE_LINK_SQL)
            .param(LAST_UPDATED_AT, link.getLastUpdatedAt())
            .param(ID, link.getId())
            .update();
    }

    @Override
    public List<Link> findLinksToUpdate() {
        return jdbcClient.sql(FIND_LINKS_TO_UPDATE_SQL)
            .query(ROW_MAPPER)
            .list();
    }
}
