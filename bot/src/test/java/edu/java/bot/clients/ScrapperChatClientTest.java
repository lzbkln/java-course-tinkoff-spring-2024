package edu.java.bot.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.bot.BotApplication;
import org.apache.kafka.clients.admin.AdminClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {BotApplication.class})
@DirtiesContext
public class ScrapperChatClientTest {
    @MockBean AdminClient adminClient;
    @MockBean KafkaAdmin kafkaAdmin;
    private static WireMockServer wireMockServer;
    @Autowired
    ScrapperChatClient scrapperChatClient;
    private static final String TG_CHAT = "/scrapper/tg-chat/%d";

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
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
    @DisplayName("Test that register chat returned valid response")
    public void testThatRegisterChatReturnedValidResponse() {
        wireMockServer.stubFor(WireMock.post(TG_CHAT.formatted(123L))
            .willReturn(WireMock.ok()));

        ResponseEntity<Void> response = scrapperChatClient.registerChat(123L).block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Test that already register chat returned valid response")
    public void testThatAlreadyRegisterChatReturnedValidResponse() {
        wireMockServer.stubFor(WireMock.post(TG_CHAT.formatted(123L))
            .willReturn(WireMock.status(HttpStatus.CONFLICT.value())));

        ResponseEntity<Void> response = scrapperChatClient.registerChat(123L).block();

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Test that delete register chat returned valid response")
    public void testThatDeleteRegisterChatReturnedValidResponse() {
        wireMockServer.stubFor(WireMock.delete(TG_CHAT.formatted(123L))
            .willReturn(WireMock.ok()));

        ResponseEntity<Void> response = scrapperChatClient.deleteChat(123L).block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Test that delete non register chat returned valid response")
    public void testThatDeleteNonRegisterChatReturnedValidResponse() {
        wireMockServer.stubFor(WireMock.delete(TG_CHAT.formatted(1L))
            .willReturn(WireMock.notFound()));

        ResponseEntity<Void> response = scrapperChatClient.deleteChat(1L).block();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

}
