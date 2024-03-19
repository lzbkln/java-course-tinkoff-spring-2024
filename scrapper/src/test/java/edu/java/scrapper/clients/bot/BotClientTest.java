package edu.java.scrapper.clients.bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.ScrapperApplication;
import edu.java.clients.bot.BotClient;
import edu.java.dto.requests.LinkUpdateRequest;
import java.util.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ScrapperApplication.class})
public class BotClientTest {
    private static WireMockServer wireMockServer;

    @Autowired
    BotClient botClient;
    private static final String BOT_ID = "/bot/update";

    @DynamicPropertySource
    public static void configureRegistry(DynamicPropertyRegistry registry) {
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
    @DisplayName("Test sending valid update request")
    public void testSendingValidUpdateRequest() {
        LinkUpdateRequest request =
            new LinkUpdateRequest(1L, "https://github.com", "Update", Collections.singletonList(123L));

        wireMockServer.stubFor(WireMock.post(BOT_ID)
            .withRequestBody(WireMock.equalToJson("""
                {
                    "id": %d,
                    "url": "%s",
                      "description": "%s",
                      "tgChatIds": [
                        %d
                      ]
                }
                """.formatted(request.id(), request.url(), request.description(), request.tgChatIds().getFirst())))
            .willReturn(WireMock.ok()));

        ResponseEntity<Void> response = botClient.sendUpdate(request).block();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}
