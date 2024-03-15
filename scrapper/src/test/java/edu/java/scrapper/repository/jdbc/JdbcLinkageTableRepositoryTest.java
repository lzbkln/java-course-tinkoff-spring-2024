package edu.java.scrapper.repository.jdbc;

import edu.java.repository.LinkageTableRepository;
import edu.java.repository.entity.LinkageTable;
import edu.java.repository.jdbc.rowMappers.LinkageTableRowMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JdbcLinkageTableRepositoryTest extends IntegrationTest {
    @Autowired
    LinkageTableRepository linkageTableRepository;
    static final RowMapper<LinkageTable> ROW_MAPPER = new LinkageTableRowMapper();

    @Test
    @DisplayName("Test that saving table relationship correctly")
    void testThatSavingTableRelationshipCorrectly() {
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (1)");
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES ('https://link.com','2022-03-21 12:52:23')");
        LinkageTable linkageTable = new LinkageTable(1L, 1L);

        linkageTableRepository.save(linkageTable);

        Optional<LinkageTable> actualChatLink =
            jdbcTemplate.query("SELECT * FROM linkage_table", ROW_MAPPER).stream().findFirst();

        assertThat(actualChatLink).isPresent();
    }

    @Test
    @DisplayName("Test that findByChatId returns correct linkage tables")
    void testThatFindByChatIdReturnsCorrectLinkageTables() {
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (1)");
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES ('https://link1','2022-06-16 16:37:23')");
        LinkageTable linkageTable = new LinkageTable(1L, 1L);

        linkageTableRepository.save(linkageTable);

        List<Long> actualLinkIds = linkageTableRepository.findByChatId(1L)
            .stream().map(LinkageTable::getLinkId).toList();

        assertThat(actualLinkIds.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test that findByLinkId returns correct linkage tables")
    void testThatFindByLinkIdReturnsCorrectLinkageTables() {
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (1)");
        jdbcTemplate.update(
            "INSERT INTO links (url, last_updated_at) VALUES ('https://link2.com','2022-06-16 16:37:23')");

        LinkageTable linkageTable = new LinkageTable(1L, 1L);
        linkageTableRepository.save(linkageTable);

        List<Long> actualChatIds = linkageTableRepository.findByLinkId(1L)
            .stream().map(LinkageTable::getLinkId).toList();

        assertThat(actualChatIds.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test that removeByChatIdAndLinkId removes successful")
    void testThatRemoveByChatIdAndLinkIdRemovesLinkageTable() {
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (1)");
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES ('https://link.com','2012-04-19 21:35:21')");
        linkageTableRepository.save(new LinkageTable(1L, 1L));

        assertThat(linkageTableRepository.findByLinkId(1L).getFirst()).isNotNull();
        linkageTableRepository.removeByChatIdAndLinkId(1L, 1L);
        assertThat(linkageTableRepository.findByLinkId(1L)).isEqualTo(List.of());
    }

    @Test
    @DisplayName("Test that CountByLinkId returns correct count")
    void testThatCountByLinkIdReturnsCorrectCount() {
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (1)");
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (2)");
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES ('https://link.com','2022-03-21 12:52:23')");
        LinkageTable linkageTable = new LinkageTable(1L, 1L);
        LinkageTable linkageTable2 = new LinkageTable(2L, 1L);

        linkageTableRepository.save(linkageTable);
        linkageTableRepository.save(linkageTable2);

        int count = linkageTableRepository.countByLinkId(1L);

        assertThat(count).isEqualTo(2);
    }
}
