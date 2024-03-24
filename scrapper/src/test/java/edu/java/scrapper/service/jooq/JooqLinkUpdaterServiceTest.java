package edu.java.scrapper.service.jooq;

import edu.java.ScrapperApplication;
import edu.java.repository.entity.Link;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.LinkUpdater;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static edu.java.domain.jooq.tables.Chats.CHATS;
import static edu.java.domain.jooq.tables.Linkage.LINKAGE;
import static edu.java.domain.jooq.tables.Links.LINKS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JooqLinkUpdaterServiceTest extends IntegrationTest {
    @Autowired
    private LinkUpdater linkUpdaterService;

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
}
