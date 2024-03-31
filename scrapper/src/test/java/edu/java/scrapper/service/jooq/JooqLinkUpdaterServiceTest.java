package edu.java.scrapper.service.jooq;

import edu.java.ScrapperApplication;
import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.entity.GithubBranches;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.StackOverflowQuestion;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static edu.java.domain.jooq.tables.Chats.CHATS;
import static edu.java.domain.jooq.tables.GithubBranches.GITHUB_BRANCHES;
import static edu.java.domain.jooq.tables.Linkage.LINKAGE;
import static edu.java.domain.jooq.tables.Links.LINKS;
import static edu.java.domain.jooq.tables.StackoverflowQuestion.STACKOVERFLOW_QUESTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JooqLinkUpdaterServiceTest extends IntegrationTest {
    @Autowired
    private LinkUpdater linkUpdaterService;
    @Autowired
    private StackOverflowQuestionRepository stackOverflowQuestionRepository;
    @Autowired
    private GithubBranchesRepository githubBranchesRepository;
    @Autowired
    private LinkUpdater linkUpdater;
    @Autowired
    private DSLContext dslContext;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JOOQ");
    }

    @Test
    @DisplayName("Test that update method updates link in database")
    void testThatUpdateMethodUpdatesLinkInDatabase() {
        URI url = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        OffsetDateTime time = OffsetDateTime.now().minusDays(1);

        Link link = new Link(url.toString());
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values(link.getUrl(), time)
            .execute();

        OffsetDateTime updatedAt = OffsetDateTime.now();
        link.setLastUpdatedAt(updatedAt);
        linkUpdaterService.update(new Link(1L, url.toString(), updatedAt));

        Link updatedLink = dslContext.selectFrom(LINKS).where(LINKS.ID.eq(1L)).fetchOneInto(Link.class);
        assertThat(updatedLink.getLastUpdatedAt()).isNotEqualTo(time);
    }

    @Test
    @DisplayName("Test that findLinksToUpdate method returns correct links")
    void testThatFindLinksToUpdateMethodReturnsCorrectLinks() {
        OffsetDateTime now = OffsetDateTime.now();
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values("https://example.com", now)
            .execute();
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values("https://example.org", now.minusMinutes(30))
            .execute();
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values("https://example.net", now.minusHours(2))
            .execute();

        List<Link> linksToUpdate = linkUpdaterService.findLinksToUpdate();

        assertThat(linksToUpdate).hasSize(1);
        assertThat(linksToUpdate.get(0).getUrl()).isEqualTo("https://example.net");
    }

    @Test
    @DisplayName("Test that findTgChatIds method returns correct chat ids")
    void testThatFindTgChatIdsMethodReturnsCorrectChatIds() {
        Long linkId = 1L;
        dslContext.insertInto(CHATS, CHATS.ID)
            .values(100L)
            .execute();
        dslContext.insertInto(CHATS, CHATS.ID)
            .values(200L)
            .execute();
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values("1", OffsetDateTime.now())
            .execute();
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values("2", OffsetDateTime.now())
            .execute();
        dslContext.insertInto(LINKAGE, LINKAGE.CHAT_ID, LINKAGE.LINK_ID)
            .values(100L, linkId)
            .execute();
        dslContext.insertInto(LINKAGE, LINKAGE.CHAT_ID, LINKAGE.LINK_ID)
            .values(200L, linkId)
            .execute();
        dslContext.insertInto(LINKAGE, LINKAGE.CHAT_ID, LINKAGE.LINK_ID)
            .values(200L, 2L)
            .execute();

        List<Long> tgChatIds = linkUpdaterService.findTgChatIds(linkId);

        assertThat(tgChatIds).containsExactlyInAnyOrder(100L, 200L);
    }

    @Test
    @DisplayName("Test that updateGitBranches method correctly updates branches")
    void testThatUpdateGitBranchesMethodCorrectlyUpdatesBranches() {
        String uri = "https://github.com/lzbkln/java-course-tinkoff-spring-2024";
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values(uri, OffsetDateTime.now())
            .execute();
        List<String> oldBranches = List.of("oldBranch1", "oldBranch2");
        dslContext.insertInto(GITHUB_BRANCHES, GITHUB_BRANCHES.LINK_ID, GITHUB_BRANCHES.BRANCHES)
            .values(1L, oldBranches.toArray(new String[0]))
            .execute();

        List<String> newBranches = Arrays.asList("branch1", "branch2", "branch3");
        linkUpdater.updateGitBranches(new Link(1L, uri, OffsetDateTime.now()), newBranches);

        GithubBranches updatedBranches = githubBranchesRepository.getByLinkId(1L);
        assertEquals(newBranches, updatedBranches.getBranches());
    }

    @Test
    @DisplayName("Test that updateAnswerCount method correctly updates answer count")
    void testThatUpdateAnswerCountMethodCorrectlyUpdatesAnswerCount() {
        String uri = "https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init";
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values(uri, OffsetDateTime.now())
            .execute();
        dslContext.insertInto(
                STACKOVERFLOW_QUESTION,
                STACKOVERFLOW_QUESTION.LINK_ID,
                STACKOVERFLOW_QUESTION.ANSWER_COUNT
            )
            .values(1L, 1L)
            .execute();

        linkUpdater.updateAnswerCount(new Link(1L, uri, OffsetDateTime.now()), 3);

        StackOverflowQuestion updatedQuestion = stackOverflowQuestionRepository.getByLinkId(1L);
        assertEquals(3, updatedQuestion.getAnswerCount());
    }
}
