package edu.java.bot.retry;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.bot.BotApplication;
import edu.java.bot.clients.ScrapperChatClient;
import edu.java.bot.configuration.retry.HttpStatusCodes;
import java.time.Duration;
import java.util.Set;
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
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static edu.java.bot.configuration.retry.RetryType.CONSTANT;

@SpringBootTest(classes = {BotApplication.class})
@DirtiesContext
public class ConstantRetryTest {
    @MockBean AdminClient adminClient;
    @MockBean KafkaAdmin kafkaAdmin;
    @Autowired
    private ScrapperChatClient scrapperChatClient;
    private static final String TG_CHAT = "/scrapper/tg-chat/%d";
    private static WireMockServer wireMockServer;

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.retry.type", () -> CONSTANT);
        registry.add(
            "app.retry.retry-on-statuses",
            () -> Set.of(HttpStatusCodes.CLIENT_ERROR, HttpStatusCodes.SERVER_ERROR)
        );
        registry.add("app.retry.max-attempts", () -> 3);
        registry.add("app.retry.delay", () -> "1s");
        registry.add("app.scrapper-link.link", wireMockServer::baseUrl);
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

    @Test
    @DisplayName("Test that constant retry works correct")
    void testThatConstantRetryWorksCorrect() {
        wireMockServer.stubFor(WireMock.post(TG_CHAT.formatted(1L))
            .willReturn(aResponse()
                .withStatus(409)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "description": "Double registration",
                      "code": "409",
                      "exceptionName": "ReRegistrationException",
                      "exceptionMessage": "Double registration",
                      "stacktrace": [
                        "string"
                      ]
                    }""")
            ));

        StepVerifier.create(scrapperChatClient.registerChat(1L))
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(1))
            .expectNoEvent(Duration.ofSeconds(1))
            .expectNoEvent(Duration.ofSeconds(1))
            .expectError()
            .verify();
    }

    @Test
    @DisplayName("Test that constant retry works correct for server error")
    void testThatConstantRetryWorksCorrectForServerError() {
        wireMockServer.stubFor(WireMock.post(TG_CHAT.formatted(1L))
            .willReturn(aResponse()
                .withStatus(500)));

        StepVerifier.create(scrapperChatClient.registerChat(1L))
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(1))
            .expectNoEvent(Duration.ofSeconds(1))
            .expectNoEvent(Duration.ofSeconds(1))
            .expectError()
            .verify();
    }
}
