package edu.java.scrapper.repository.jdbc;

import edu.java.repository.LinkRepository;
import edu.java.repository.entity.Link;
import edu.java.repository.jdbc.rowMappers.LinkRowMapper;
import edu.java.scrapper.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    LinkRepository linkRepository;
    static final RowMapper<Link> ROW_MAPPER = new LinkRowMapper();

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JDBC");
    }

    @Test
    @DisplayName("Test that saving table relationship correctly")
    void testThatSavingTableRelationshipCorrectly() {
        Link link = new Link("https://example.com");
        linkRepository.save(link);

        int actualSize =
            jdbcTemplate.query("SELECT * FROM links", ROW_MAPPER).size();

        assertThat(actualSize).isEqualTo(1);
    }

    @Test
    @DisplayName("Test that findById returns correct link")
    void testThatFindByIdReturnsCorrectLink() {
        Link link = new Link("https://example.com");
        linkRepository.save(link);

        assertThat(linkRepository.findById(1L).getUrl()).isEqualTo(link.getUrl());
    }

    @Test
    @DisplayName("Test that findByUrl returns correct link")
    void testThatFindByUrlReturnsCorrectLink() {
        Link link = new Link("https://example.com");
        linkRepository.save(link);

        assertThat(linkRepository.findByUrl(link.getUrl()).getId()).isEqualTo(1L);
        assertThat(linkRepository.findByUrl(link.getUrl()).getUrl()).isEqualTo(link.getUrl());
    }

    @Test
    @DisplayName("Test that removeById removes correctly")
    void testThatRemoveByIdRemovesCorrectly() {
        Link link = new Link("https://example.com");
        linkRepository.save(link);

        assertThat(linkRepository.findByUrl(link.getUrl())).isNotNull();
        linkRepository.removeById(1L);
        assertThrows(EmptyResultDataAccessException.class, () -> linkRepository.findByUrl(link.getUrl()));
    }
}
