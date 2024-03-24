package edu.java.scrapper.service.jooq;

import edu.java.ScrapperApplication;
import edu.java.dto.responses.ListLinksResponse;
import edu.java.repository.entity.GithubBranches;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.Linkage;
import edu.java.repository.entity.StackOverflowQuestion;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.LinkService;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import java.net.URI;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JooqLinkServiceTest extends IntegrationTest {
    @Autowired
    private LinkService linkService;
    @Autowired
    private DSLContext dslContext;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JOOQ");
    }

    @Test
    @DisplayName("Test that save link works correctly")
    void testThatSaveLinkWorksCorrectly() {
        Long tgChatId = 1L;
        URI gitUrl = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI stackUrl =
            URI.create("https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init");
        dslContext.insertInto(CHATS, CHATS.ID)
            .values(tgChatId)
            .execute();

        linkService.saveLink(tgChatId, gitUrl);
        linkService.saveLink(tgChatId, stackUrl);

        List<Linkage> linkage = dslContext.selectFrom(LINKAGE)
            .where(LINKAGE.CHAT_ID.eq(tgChatId))
            .fetchInto(Linkage.class);
        assertEquals(2, linkage.size());

        List<Link> links = dslContext.selectFrom(LINKS)
            .fetchInto(Link.class);
        assertEquals(2, links.size());

        List<StackOverflowQuestion> stackOverflowQuestions = dslContext.selectFrom(STACKOVERFLOW_QUESTION)
            .fetchInto(StackOverflowQuestion.class);
        assertEquals(1, stackOverflowQuestions.size());
    }

    @Test
    @DisplayName("Test that delete link works correctly")
    void testThatDeleteLinkWorksCorrectly() {
        Long tgChatId = 1L;
        URI gitUrl = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI stackUrl =
            URI.create("https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init");
        dslContext.insertInto(CHATS, CHATS.ID)
            .values(tgChatId)
            .execute();
        linkService.saveLink(tgChatId, gitUrl);
        linkService.saveLink(tgChatId, stackUrl);

        linkService.deleteLink(tgChatId, gitUrl);
        linkService.deleteLink(tgChatId, stackUrl);

        List<Linkage> linkage = dslContext.selectFrom(LINKAGE)
            .where(LINKAGE.CHAT_ID.eq(tgChatId))
            .fetchInto(Linkage.class);
        assertEquals(0, linkage.size());

        List<Link> links = dslContext.selectFrom(LINKS)
            .fetchInto(Link.class);
        assertEquals(0, links.size());

        List<GithubBranches> githubBranches = dslContext.selectFrom(GITHUB_BRANCHES)
            .fetchInto(GithubBranches.class);
        assertEquals(0, githubBranches.size());

        List<StackOverflowQuestion> stackOverflowQuestions = dslContext.selectFrom(STACKOVERFLOW_QUESTION)
            .fetchInto(StackOverflowQuestion.class);
        assertEquals(0, stackOverflowQuestions.size());
    }

    @Test
    @DisplayName("Test that get all links response works correctly")
    void testThatGetAllLinksResponseWorksCorrectly() {
        Long tgChatId = 1L;
        URI gitUrl = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI stackUrl =
            URI.create("https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init");
        dslContext.insertInto(CHATS, CHATS.ID)
            .values(tgChatId)
            .execute();
        linkService.saveLink(tgChatId, gitUrl);
        linkService.saveLink(tgChatId, stackUrl);

        ListLinksResponse response = linkService.getAllLinksResponse(tgChatId);

        assertEquals(2, response.links().size());
        assertTrue(response.links().stream().anyMatch(linkResponse -> linkResponse.url().equals(gitUrl)));
        assertTrue(response.links().stream().anyMatch(linkResponse -> linkResponse.url().equals(stackUrl)));
    }

    @Test
    @DisplayName("Test that get all links response throws NonRegisterChatException")
    void testThatGetAllLinksResponseThrowsNonRegisterChatException() {
        Long tgChatId = 1L;

        assertThrows(NonRegisterChatException.class, () -> linkService.getAllLinksResponse(tgChatId));
    }

}
