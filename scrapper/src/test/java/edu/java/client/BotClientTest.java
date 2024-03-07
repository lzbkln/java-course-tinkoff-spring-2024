package edu.java.client;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ScrapperApplication.class})
public class BotClientTest {
    private static WireMockServer wireMockServer;
    private static BotClient botClient;
    private static final String BOT_ID = "/bot/update";

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
        botClient = new BotClient("http://localhost:" + wireMockServer.port());
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
