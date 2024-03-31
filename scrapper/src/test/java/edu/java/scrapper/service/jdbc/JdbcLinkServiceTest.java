package edu.java.scrapper.service.jdbc;

import edu.java.ScrapperApplication;
import edu.java.dto.responses.ListLinksResponse;
import edu.java.repository.entity.GithubBranches;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.Linkage;
import edu.java.repository.entity.StackOverflowQuestion;
import edu.java.repository.jdbc.rowMappers.GithubBranchesRowMapper;
import edu.java.repository.jdbc.rowMappers.LinkRowMapper;
import edu.java.repository.jdbc.rowMappers.LinkageRowMapper;
import edu.java.repository.jdbc.rowMappers.StackOverflowQuestionRowMapper;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.LinkService;
import edu.java.service.exceptions.AlreadyTrackedLinkException;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JdbcLinkServiceTest extends IntegrationTest {
    @Autowired
    private LinkService linkService;
    private static final RowMapper<Link> LINK_ROW_MAPPER = new LinkRowMapper();
    private static final RowMapper<Linkage> LINKAGE_ROW_MAPPER = new LinkageRowMapper();
    private static final RowMapper<GithubBranches> GITHUB_ROW_MAPPER = new GithubBranchesRowMapper();
    private static final RowMapper<StackOverflowQuestion> STACK_OVERFLOW_ROW_MAPPER =
        new StackOverflowQuestionRowMapper();

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JDBC");
    }

    @Test
    @DisplayName("Test that save link works correctly")
    void testThatSaveLinkWorksCorrectly() {
        Long tgChatId = 1L;
        URI gitUrl = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI stackUrl =
            URI.create("https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init");
        jdbcTemplate.update("INSERT INTO chats (id) VALUES (?)", tgChatId);

        linkService.saveLink(tgChatId, gitUrl);
        linkService.saveLink(tgChatId, stackUrl);

        List<Linkage> linkage = jdbcTemplate.query(
            "SELECT * FROM linkage WHERE chat_id = ?",
            LINKAGE_ROW_MAPPER,
            tgChatId
        );
        assertEquals(2, linkage.size());

        List<Link> links = jdbcTemplate.query(
            "SELECT * FROM links",
            LINK_ROW_MAPPER
        );
        assertEquals(2, links.size());

        List<StackOverflowQuestion> stackOverflowQuestions = jdbcTemplate.query(
            "SELECT * FROM stackoverflow_question",
            STACK_OVERFLOW_ROW_MAPPER
        );
        assertEquals(1, stackOverflowQuestions.size());
    }

    @Test
    @DisplayName("Test that delete link works correctly")
    void testThatDeleteLinkWorksCorrectly() {
        Long tgChatId = 1L;
        URI gitUrl = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI stackUrl =
            URI.create("https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init");
        jdbcTemplate.update("INSERT INTO chats (id) VALUES (?)", tgChatId);

        linkService.saveLink(tgChatId, gitUrl);
        linkService.saveLink(tgChatId, stackUrl);
        linkService.deleteLink(tgChatId, gitUrl);
        linkService.deleteLink(tgChatId, stackUrl);

        List<Link> links = jdbcTemplate.query(
            "SELECT * FROM linkage WHERE chat_id = ?",
            LINK_ROW_MAPPER,
            tgChatId
        );
        assertEquals(0, links.size());

        List<GithubBranches> githubBranches = jdbcTemplate.query(
            "SELECT * FROM github_branches",
            GITHUB_ROW_MAPPER
        );
        assertEquals(0, githubBranches.size());

        List<StackOverflowQuestion> stackOverflowQuestions = jdbcTemplate.query(
            "SELECT * FROM stackoverflow_question",
            STACK_OVERFLOW_ROW_MAPPER
        );
        assertEquals(0, stackOverflowQuestions.size());
    }

    @Test
    @DisplayName("Test that get all links response works correctly")
    void testThatGetAllLinksResponseWorksCorrectly() {
        Long tgChatId = 1L;
        URI url1 = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI url2 = URI.create("https://github.com/lzbkln/java-course-2023-tink");
        jdbcTemplate.update("INSERT INTO chats (id) VALUES (?)", tgChatId);

        linkService.saveLink(tgChatId, url1);
        linkService.saveLink(tgChatId, url2);

        ListLinksResponse response = linkService.getAllLinksResponse(tgChatId);

        assertEquals(2, response.links().size());
        assertTrue(response.links().stream().anyMatch(linkResponse -> linkResponse.url().equals(url1)));
        assertTrue(response.links().stream().anyMatch(linkResponse -> linkResponse.url().equals(url2)));
    }

    @Test
    @DisplayName("Test that save link throws NoSuchLinkException")
    void testThatSaveLinkThrowsNoSuchLinkException() {
        Long tgChatId = 1L;
        URI url = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        jdbcTemplate.update("INSERT INTO chats (id) VALUES (?)", tgChatId);

        assertThrows(NoSuchLinkException.class, () -> linkService.deleteLink(tgChatId, url));
    }

    @Test
    @DisplayName("Test that get all links response throws NonRegisterChatException")
    void testThatGetAllLinksResponseThrowsNonRegisterChatException() {
        Long tgChatId = 1L;

        assertThrows(NonRegisterChatException.class, () -> linkService.getAllLinksResponse(tgChatId));
    }

    @Test
    @DisplayName("Test that delete link throws NonRegisterChatException")
    void testThatDeleteLinkThrowsNonRegisterChatException() {
        Long tgChatId = 1L;

        assertThrows(NonRegisterChatException.class, () -> linkService.deleteLink(tgChatId, URI.create("")));
    }

    @Test
    @DisplayName("Test that save link throws NonRegisterChatException")
    void testThatSaveLinkThrowsNonRegisterChatException() {
        Long tgChatId = 1L;

        assertThrows(NonRegisterChatException.class, () -> linkService.saveLink(tgChatId, URI.create("")));
    }

    @Test
    @DisplayName("Test that save link throws AlreadyTrackedLinkException")
    void testThatSaveLinkThrowsAlreadyTrackedLinkException() {
        Long tgChatId = 1L;
        URI uri = URI.create("https://github.com");
        jdbcTemplate.update("INSERT INTO chats (id) VALUES (?)", tgChatId);
        linkService.saveLink(tgChatId, uri);

        assertThrows(AlreadyTrackedLinkException.class, () -> linkService.saveLink(tgChatId, uri));
    }
}
