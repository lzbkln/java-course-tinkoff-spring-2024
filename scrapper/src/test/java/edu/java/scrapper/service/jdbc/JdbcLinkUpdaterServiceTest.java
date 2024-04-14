package edu.java.scrapper.service.jdbc;

import edu.java.ScrapperApplication;
import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.entity.GithubBranches;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.StackOverflowQuestion;
import edu.java.repository.jdbc.rowMappers.LinkRowMapper;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ScrapperApplication.class})
@DirtiesContext
public class JdbcLinkUpdaterServiceTest extends IntegrationTest {

    private static final RowMapper<Link> LINK_ROW_MAPPER = new LinkRowMapper();

    @Autowired
    private LinkUpdater linkUpdaterService;
    @Autowired
    private StackOverflowQuestionRepository stackOverflowQuestionRepository;
    @Autowired
    private GithubBranchesRepository githubBranchesRepository;
    @Autowired
    private LinkUpdater linkUpdater;

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

    @Test
    @DisplayName("Test that updateGitBranches method correctly updates branches")
    void testThatUpdateGitBranchesMethodCorrectlyUpdatesBranches() {
        String uri = "https://github.com/lzbkln/java-course-tinkoff-spring-2024";
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES (?, ?)", uri, OffsetDateTime.now());
        List<String> oldBranches = List.of("oldBranch1", "oldBranch2");
        jdbcTemplate.update(
            "INSERT INTO github_branches (link_id, branches) VALUES (?, ?)",
            1L,
            oldBranches.toArray(new String[0])
        );

        List<String> newBranches = Arrays.asList("branch1", "branch2", "branch3");
        linkUpdater.updateGitBranches(new Link(1L, uri, OffsetDateTime.now()), newBranches);

        GithubBranches updatedBranches = githubBranchesRepository.getByLinkId(1L);
        assertEquals(newBranches, updatedBranches.getBranches());
    }

    @Test
    @DisplayName("Test that updateAnswerCount method correctly updates answer count")
    void testThatUpdateAnswerCountMethodCorrectlyUpdatesAnswerCount() {
        String uri = "https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init";
        jdbcTemplate.update("INSERT INTO links (url, last_updated_at) VALUES (?, ?)", uri, OffsetDateTime.now());
        jdbcTemplate.update("INSERT INTO stackoverflow_question (link_id, answer_count) VALUES (?, ?)", 1L, 2);

        linkUpdater.updateAnswerCount(new Link(1L, uri, OffsetDateTime.now()), 3);

        StackOverflowQuestion updatedQuestion = stackOverflowQuestionRepository.getByLinkId(1L);
        assertEquals(3, updatedQuestion.getAnswerCount());
    }
}
