package edu.java.scrapper.service.jpa;

import edu.java.ScrapperApplication;
import edu.java.dto.responses.ListLinksResponse;
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
import edu.java.service.exceptions.AlreadyTrackedLinkException;
import edu.java.service.exceptions.NoSuchLinkException;
import edu.java.service.exceptions.NonRegisterChatException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
public class JpaLinkServiceTest extends IntegrationTest {
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

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JPA");
    }

    @Test
    @DisplayName("Test that delete link works correctly")
    void testThatDeleteLinkWorksCorrectly() {
        Long tgChatId = 1L;
        URI gitUrl = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI stackUrl =
            URI.create("https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init");
        jpaTelegramChatRepository.save(new JpaTelegramChat(tgChatId));

        linkService.saveLink(tgChatId, gitUrl);
        linkService.saveLink(tgChatId, stackUrl);
        linkService.deleteLink(tgChatId, gitUrl);

        List<JpaGithubBranches> githubBranches = jpaGithubBranchesRepository.findAll();
        assertEquals(0, githubBranches.size());

        List<JpaStackOverflowQuestion> stackOverflowQuestions = jpaStackOverflowQuestionRepository.findAll();
        assertEquals(1, stackOverflowQuestions.size());

        List<JpaLinkage> linkage = jpaLinkageRepository.findAll();
        assertEquals(1, linkage.size());

        List<JpaLink> links = jpaLinkRepository.findAll();
        assertEquals(1, links.size());
    }

    @Test
    @DisplayName("Test that get all links response works correctly")
    void testThatGetAllLinksResponseWorksCorrectly() {
        Long tgChatId = 1L;
        URI url1 = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI url2 = URI.create("https://github.com/lzbkln/java-course-2023-tink");
        jpaTelegramChatRepository.save(new JpaTelegramChat(tgChatId));

        linkService.saveLink(tgChatId, url1);
        linkService.saveLink(tgChatId, url2);

        ListLinksResponse response = linkService.getAllLinksResponse(tgChatId);

        assertEquals(2, response.links().size());
        assertTrue(response.links().stream().anyMatch(linkResponse -> linkResponse.url().equals(url1)));
        assertTrue(response.links().stream().anyMatch(linkResponse -> linkResponse.url().equals(url2)));
    }

    @Test
    @DisplayName("Test that save link works correctly")
    void testThatSaveLinkWorksCorrectly() {
        Long tgChatId = 1L;
        URI gitUrl = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        URI stackUrl =
            URI.create("https://stackoverflow.com/questions/59715622/docker-compose-and-create-db-in-postgres-on-init");
        jpaTelegramChatRepository.save(new JpaTelegramChat(tgChatId));

        linkService.saveLink(tgChatId, gitUrl);
        linkService.saveLink(tgChatId, stackUrl);

        List<JpaLinkage> linkage = jpaLinkageRepository.findAll();
        assertEquals(2, linkage.size());

        List<JpaLink> links = jpaLinkRepository.findAll();
        assertEquals(2, links.size());

        List<JpaStackOverflowQuestion> stackOverflowQuestions = jpaStackOverflowQuestionRepository.findAll();
        assertEquals(1, stackOverflowQuestions.size());
    }

    @Test
    @DisplayName("Test that save link throws NoSuchLinkException")
    void testThatSaveLinkThrowsNoSuchLinkException() {
        Long tgChatId = 1L;
        URI url = URI.create("https://github.com/lzbkln/java-course-tinkoff-spring-2024");
        jpaTelegramChatRepository.save(new JpaTelegramChat(tgChatId));

        assertThrows(NoSuchLinkException.class, () -> linkService.deleteLink(tgChatId, url));
    }

    @Test
    @DisplayName("Test that get all links response throws NonRegisterChatException")
    void testThatGetAllLinksResponseThrowsNonRegisterChatException() {
        Long tgChatId = 1L;

        assertThrows(NonRegisterChatException.class, () -> linkService.getAllLinksResponse(tgChatId));
    }

    @Test
    @DisplayName("Test that save link throws NonRegisterChatException")
    void testThatSaveLinkThrowsNonRegisterChatException() {
        Long tgChatId = 1L;

        assertThrows(NonRegisterChatException.class, () -> linkService.saveLink(tgChatId, URI.create("")));
    }

    @Test
    @DisplayName("Test that delete link throws NonRegisterChatException")
    void testThatDeleteLinkThrowsNonRegisterChatException() {
        Long tgChatId = 1L;

        assertThrows(NonRegisterChatException.class, () -> linkService.deleteLink(tgChatId, URI.create("")));
    }

    @Test
    @DisplayName("Test that double save link throws AlreadyTrackedLinkException")
    void testThatDoubleSaveLinkThrowsNoSuchLinkException() {
        Long tgChatId = 1L;
        Long linkId = 1L;
        jpaTelegramChatRepository.save(new JpaTelegramChat(tgChatId));
        jpaLinkRepository.save(new JpaLink(linkId, "url1", OffsetDateTime.now()));
        jpaLinkageRepository.save(new JpaLinkage(new JpaTelegramChat(tgChatId), new JpaLink(linkId)));

        assertThrows(AlreadyTrackedLinkException.class, () -> linkService.saveLink(tgChatId, URI.create("url1")));
    }
}
