package edu.java.scrapper.service.jdbc;

import edu.java.ScrapperApplication;
import edu.java.repository.entity.Link;
import edu.java.repository.jdbc.rowMappers.LinkRowMapper;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JdbcLinkUpdaterServiceTest extends IntegrationTest {

    private static final RowMapper<Link> LINK_ROW_MAPPER = new LinkRowMapper();

    @Autowired
    private LinkUpdater linkUpdaterService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JDBC");
    }

    @Test
    @DisplayName("Test that update method updates link in database")
    void testThatUpdateMethodUpdatesLinkInDatabase() {
        URI url = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        OffsetDateTime time = OffsetDateTime.now(ZoneOffset.UTC).minusDays(1);

        Link link = new Link(url.toString());
        jdbcTemplate.update(
            "INSERT INTO links (url, last_updated_at) VALUES (?, ?)",
            link.getUrl(),
            time
        );

        OffsetDateTime updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
        link.setLastUpdatedAt(updatedAt);
        linkUpdaterService.update(new Link(1L, url.toString(), updatedAt));

        Link updatedLink =
            jdbcTemplate.queryForObject("SELECT * FROM links WHERE id = ?", LINK_ROW_MAPPER, 1L);
        assertThat(updatedLink.getLastUpdatedAt()).isNotEqualTo(time);
    }

    @Test
    @DisplayName("Test that findLinksToUpdate method returns correct links")
    void testThatFindLinksToUpdateMethodReturnsCorrectLinks() {
        OffsetDateTime now = OffsetDateTime.now();
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES (?, ?)", "https://example.com", now);
        jdbcTemplate.update(
            "INSERT INTO links (url, last_updated_at) VALUES (?, ?)",
            "https://example.org",
            now.minusMinutes(30)
        );
        jdbcTemplate.update(
            "INSERT INTO links (url, last_updated_at) VALUES (?, ?)",
            "https://example.net",
            now.minusHours(2)
        );

        List<Link> linksToUpdate = linkUpdaterService.findLinksToUpdate();

        assertThat(linksToUpdate).hasSize(1);
        assertThat(linksToUpdate.get(0).getUrl()).isEqualTo("https://example.net");
    }

    @Test
    @DisplayName("Test that findTgChatIds method returns correct chat ids")
    void testThatFindTgChatIdsMethodReturnsCorrectChatIds() {
        Long linkId = 1L;
        jdbcTemplate.update("INSERT INTO chats (id) VALUES (?)", 100L);
        jdbcTemplate.update("INSERT INTO chats (id) VALUES (?)", 200L);
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES (?, ?)", "1", OffsetDateTime.now());
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES (?, ?)", "2", OffsetDateTime.now());
        jdbcTemplate.update("INSERT INTO linkage (chat_id, link_id) VALUES (?, ?)", 100L, linkId);
        jdbcTemplate.update("INSERT INTO linkage (chat_id, link_id) VALUES (?, ?)", 200L, linkId);
        jdbcTemplate.update("INSERT INTO linkage (chat_id, link_id) VALUES (?, ?)", 200L, 2L);

        List<Long> tgChatIds = linkUpdaterService.findTgChatIds(linkId);

        assertThat(tgChatIds).containsExactlyInAnyOrder(100L, 200L);
    }
}
