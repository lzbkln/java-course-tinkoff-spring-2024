package edu.java.scrapper.retry;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.ScrapperApplication;
import edu.java.clients.bot.BotClient;
import edu.java.configuration.retry.HttpStatusCodes;
import edu.java.dto.requests.LinkUpdateRequest;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static edu.java.configuration.retry.RetryType.EXPONENTIAL;

@SpringBootTest(classes = {ScrapperApplication.class})
@DirtiesContext
public class ExponentialRetryTest {
    @Autowired
    private BotClient botClient;
    private static final String POST = "/bot/update";
    private static WireMockServer wireMockServer;

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.retry.type", () -> EXPONENTIAL);
        registry.add(
            "app.retry.retry-on-statuses",
            () -> Set.of(HttpStatusCodes.SERVER_ERROR, HttpStatusCodes.CLIENT_ERROR)
        );
        registry.add("app.retry.max-attempts", () -> 3);
        registry.add("app.retry.delay", () -> "1s");
        registry.add("app.bot-link.link", wireMockServer::baseUrl);
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
    @DisplayName("Test that exponential retry works correct for client error")
    void testThatExponentialRetryWorksCorrectForClientError() {
        LinkUpdateRequest request = new LinkUpdateRequest(1L, "url", "description", List.of(1L));
        wireMockServer.stubFor(WireMock.post(POST)
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "description": "",
                      "code": "400",
                      "exceptionName": "",
                      "exceptionMessage": "",
                      "stacktrace": [
                        "string"
                      ]
                    }""")
            ));

        StepVerifier.create(botClient.sendUpdate(request))
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(1))
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(4))
            .expectError()
            .verify();
    }

    @Test
    @DisplayName("Test that exponential retry works correct for server error")
    void testThatExponentialRetryWorksCorrectForServerError() {
        LinkUpdateRequest request = new LinkUpdateRequest(1L, "url", "description", List.of(1L));
        wireMockServer.stubFor(WireMock.post(POST)
            .willReturn(aResponse()
                .withStatus(500)));

        StepVerifier.create(botClient.sendUpdate(request))
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(1))
            .expectNoEvent(Duration.ofSeconds(2))
            .expectNoEvent(Duration.ofSeconds(4))
            .expectError()
            .verify();
    }
}
