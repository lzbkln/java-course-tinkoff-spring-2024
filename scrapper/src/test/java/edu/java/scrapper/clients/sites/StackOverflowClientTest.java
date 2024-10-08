package edu.java.scrapper.clients.sites;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.ScrapperApplication;
import edu.java.clients.sites.StackOverflowClient;
import edu.java.dto.responses.StackOverflowResponseDTO;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import org.apache.kafka.clients.admin.AdminClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class})
@DirtiesContext
public class StackOverflowClientTest {
    @MockBean AdminClient adminClient;
    @MockBean KafkaAdmin kafkaAdmin;

    private static WireMockServer wireMockServer;

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.stack-overflow-url.default-url", wireMockServer::baseUrl);
    }

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
