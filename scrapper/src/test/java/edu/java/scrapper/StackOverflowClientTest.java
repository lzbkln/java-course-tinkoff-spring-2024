package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.clients.StackOverflowClient;
import edu.java.dto.StackOverflowResponseDTO;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
public class StackOverflowClientTest {

    private static WireMockServer wireMockServer;
    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Test
    @DisplayName("Test for StackOverflow existing question")
    void testThatExistingQuestionReturnedRightAnswer() {
        String id = "7520063";
        int answerCount = 1;
        int last_activity_date = 1708890424;
        OffsetDateTime lastActivityDate =
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(last_activity_date), ZoneId.of("Z"));
        String title = "fill input text";

        wireMockServer.stubFor(WireMock.get("/questions/" + id + "?site=stackoverflow")
            .willReturn(WireMock.ok()
                .withHeader("Content-type", "application/json")
                .withBody("""
                    {
                        "items": [
                            {
                                "title": "%s",
                                "answer_count": %d,
                                "last_activity_date": %d
                            }
                        ]
                    }
                    """.formatted(title, answerCount, last_activity_date))));

        StackOverflowResponseDTO response = stackOverflowClient.getQuestionsInfo(id).block();

        Objects.requireNonNull(response);
        assertEquals(title, response.items().getFirst().title());
        assertEquals(answerCount, response.items().getFirst().answerCount());
        assertEquals(lastActivityDate, response.items().getFirst().lastActivityDate());
    }

    @Test
    @DisplayName("Test for StackOverflow not existing question")
    void testThatNonExistingQuestionReturnedRightAnswer() {
        String id = "0";

        wireMockServer.stubFor(WireMock.get("/questions/" + id + "?site=stackoverflow")
            .willReturn(WireMock.ok()
                .withHeader("Content-type", "application/json")
                .withBody("""
                    {
                        "items": []
                    }
                    """)));

        StackOverflowResponseDTO response = stackOverflowClient.getQuestionsInfo(id).block();

        Objects.requireNonNull(response);
        assertTrue(response.items().isEmpty());
    }
}
