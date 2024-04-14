package edu.java.scrapper.service.jpa;

import edu.java.ScrapperApplication;
import edu.java.clients.sites.util.Utils;
import edu.java.repository.jpa.JpaGithubBranchesRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaLinkageRepository;
import edu.java.repository.jpa.JpaStackOverflowQuestionRepository;
import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.repository.jpa.entity.JpaGithubBranches;
import edu.java.repository.jpa.entity.JpaLink;
import edu.java.repository.jpa.entity.JpaLinkage;
import edu.java.repository.jpa.entity.JpaStackOverflowQuestion;
import edu.java.repository.jpa.entity.JpaTelegramChat;
import edu.java.scrapper.integration.IntegrationTest;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ScrapperApplication.class})
@DirtiesContext
public class JpaLinkUpdaterServiceTest extends IntegrationTest {
    @Autowired
    private LinkService linkService;
    @Autowired
    private JpaTelegramChatRepository jpaTelegramChatRepository;
    @Autowired
    private JpaStackOverflowQuestionRepository jpaStackOverflowQuestionRepository;
    @Autowired
    private JpaGithubBranchesRepository jpaGithubBranchesRepository;
    @Autowired
    private JpaLinkageRepository jpaLinkageRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private LinkUpdater jpaLinkUpdater;
    @Autowired Utils utils;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JPA");
    }

    @Test
    @DisplayName("Test that update method correctly saves link")
    void testThatUpdateMethodCorrectlySavesLink() {
        jpaLinkRepository.save(new JpaLink("test"));
        JpaLink link = new JpaLink(1L, "test", OffsetDateTime.now());

        jpaLinkUpdater.update(link);

        JpaLink savedLink = jpaLinkRepository.findById(1L).orElseThrow();
        assertEquals(link.getUrl(), savedLink.getUrl());
        assertEquals(link.getLastUpdatedAt(), savedLink.getLastUpdatedAt());
    }

    @Test
    @DisplayName("Test that findLinksToUpdate method returns correct links")
    void testThatFindLinksToUpdateMethodReturnsCorrectLinks() {
        OffsetDateTime now = OffsetDateTime.now();
        jpaLinkRepository.save(new JpaLink(null, "https://github.com/user/repo1", now.minusHours(2)));
        jpaLinkRepository.save(new JpaLink(null, "https://github.com/user/repo2", now.minusHours(3)));
        jpaLinkRepository.save(new JpaLink(null, "https://github.com/user/repo3", now.minusMinutes(30)));

        List<JpaLink> linksToUpdate = jpaLinkUpdater.findLinksToUpdate();

        assertEquals(2, linksToUpdate.size());
        assertEquals("https://github.com/user/repo1", linksToUpdate.get(0).getUrl());
        assertEquals("https://github.com/user/repo2", linksToUpdate.get(1).getUrl());
    }

    @Test
    @DisplayName("Test that findTgChatIds method returns correct chat ids")
    void testThatFindTgChatIdsMethodReturnsCorrectChatIds() {
        JpaTelegramChat chat = new JpaTelegramChat(1L);
        JpaTelegramChat chat2 = new JpaTelegramChat(2L);
        jpaTelegramChatRepository.save(new JpaTelegramChat(1L));
        jpaTelegramChatRepository.save(new JpaTelegramChat(2L));
        JpaLink link = new JpaLink(null, "test", OffsetDateTime.now());
        jpaLinkRepository.save(link);
        jpaLinkageRepository.save(new JpaLinkage(chat, link));
        jpaLinkageRepository.save(new JpaLinkage(chat2, link));

        List<Long> chatIds = jpaLinkUpdater.findTgChatIds(1L);

        assertEquals(2, chatIds.size());
        assertEquals(1L, chatIds.get(0).longValue());
        assertEquals(2L, chatIds.get(1).longValue());
    }

    @Test
    @DisplayName("Test that updateGitBranches method correctly updates branches")
    void testThatUpdateGitBranchesMethodCorrectlyUpdatesBranches() {
        URI uri = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        jpaLinkRepository.save(new JpaLink(uri.toString()));
        Long linkId = jpaLinkRepository.findByUrl(uri.toString()).get().getId();
        JpaLink link = new JpaLink(linkId);

        JpaGithubBranches githubBranches = new JpaGithubBranches();
        githubBranches.setLinkId(link);
        githubBranches.setBranches(Arrays.asList("oldBranch1", "oldBranch2"));
        jpaGithubBranchesRepository.save(githubBranches);

        List<String> newBranches = Arrays.asList("branch1", "branch2", "branch3");
        jpaLinkUpdater.updateGitBranches(link, newBranches);

        JpaGithubBranches updatedBranches = jpaGithubBranchesRepository.findByLinkId(link).orElseThrow();
        assertEquals(newBranches, updatedBranches.getBranches());
    }

    @Test
    @DisplayName("Test that updateAnswerCount method correctly updates answer count")
    void testThatUpdateAnswerCountMethodCorrectlyUpdatesAnswerCount() {
        jpaTelegramChatRepository.save(new JpaTelegramChat(1L));
        linkService.saveLink(
            1L,
            URI.create("https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init")
        );
        JpaLink link = new JpaLink(1L);
        jpaLinkUpdater.updateAnswerCount(link, 3);

        JpaStackOverflowQuestion updatedQuestion = jpaStackOverflowQuestionRepository.findByLinkId(link).orElseThrow();

        assertEquals(3, updatedQuestion.getAnswerCount());
    }
}
