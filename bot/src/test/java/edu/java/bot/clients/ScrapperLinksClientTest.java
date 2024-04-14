package edu.java.bot.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.bot.BotApplication;
import edu.java.bot.dto.requests.AddLinkRequest;
import edu.java.bot.dto.requests.RemoveLinkRequest;
import edu.java.bot.dto.responses.LinkResponse;
import edu.java.bot.dto.responses.ListLinksResponse;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {BotApplication.class})
@DirtiesContext
public class ScrapperLinksClientTest {

    private static final Long NON_REGISTER_CHAT_ID = 0L;
    private static final Long REGISTER_CHAT_ID = 2L;
    private static final String TG_CHAT_ID = "Tg-Chat-Id";
    private static final String SCRAPPER_LINK_URI = "/scrapper/links";

    private static WireMockServer wireMockServer;
    @Autowired
    ScrapperLinksClient scrapperLinksClient;

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
    @DisplayName("Test that get all links with register chat returned valid response")
    public void testThatGetAllLinksWithRegisterChatReturnedValidResponse() {
        final ListLinksResponse listLinks =
            new ListLinksResponse(List.of(new LinkResponse(REGISTER_CHAT_ID, URI.create("https://github.com"))), 1);

        wireMockServer.stubFor(WireMock.get(SCRAPPER_LINK_URI)
            .withHeader(TG_CHAT_ID, containing(String.valueOf(REGISTER_CHAT_ID)))
            .willReturn(WireMock.ok()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "links": [
                            {
                                "id": %d,
                                "url": "%s"
                            }
                        ],
                        "size": %d
                    }
                    """.formatted(
                    listLinks.links().getFirst().id(),
                    listLinks.links().getFirst().url().toString(),
                    listLinks.size()
                ))));

        ResponseEntity<ListLinksResponse> response = scrapperLinksClient.getAllLinks(REGISTER_CHAT_ID).block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listLinks, response.getBody());
    }

    @Test
    @DisplayName("Test that get all links with non-registered chat returned NOT_FOUND error")
    public void testThatGetAllLinksWithNonRegisterChatReturnedNotFoundError() {
        wireMockServer.stubFor(WireMock.get(SCRAPPER_LINK_URI)
            .withHeader(TG_CHAT_ID, containing(String.valueOf(NON_REGISTER_CHAT_ID)))
            .willReturn(WireMock.notFound()));

        ResponseEntity<ListLinksResponse> responseMono = scrapperLinksClient.getAllLinks(NON_REGISTER_CHAT_ID).block();

        assertNotNull(responseMono);
        assertEquals(HttpStatus.NOT_FOUND, responseMono.getStatusCode());
    }

    @Test
    @DisplayName("Test that add link with non-registered chat returned NOT_FOUND error")
    public void testThatAddLinkWithNonRegisterChatReturnedNotFoundError() {
        AddLinkRequest request = new AddLinkRequest(URI.create("https://github.com"));

        wireMockServer.stubFor(WireMock.post(SCRAPPER_LINK_URI)
            .withHeader(TG_CHAT_ID, containing(String.valueOf(NON_REGISTER_CHAT_ID)))
            .willReturn(WireMock.notFound()));

        ResponseEntity<Void> response = scrapperLinksClient.addLink(NON_REGISTER_CHAT_ID, request).block();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that add already track link returned CONFLICT error")
    public void testThatAddAlreadyTrackLinkReturnedConflictError() {
        AddLinkRequest request = new AddLinkRequest(URI.create("https://github.com"));

        wireMockServer.stubFor(WireMock.post(SCRAPPER_LINK_URI)
            .withHeader(TG_CHAT_ID, containing(String.valueOf(REGISTER_CHAT_ID)))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(request.link().toString())))
            .willReturn(WireMock.status(HttpStatus.CONFLICT.value())));

        ResponseEntity<Void> response = scrapperLinksClient.addLink(REGISTER_CHAT_ID, request).block();

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that add track link returned valid response")
    public void testThatAddTrackLinkReturnedValidResponse() {
        AddLinkRequest request = new AddLinkRequest(URI.create("https://github.com"));

        wireMockServer.stubFor(WireMock.post(SCRAPPER_LINK_URI)
            .withHeader(TG_CHAT_ID, containing(String.valueOf(REGISTER_CHAT_ID)))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(request.link().toString())))
            .willReturn(WireMock.ok()));

        ResponseEntity<Void> response = scrapperLinksClient.addLink(REGISTER_CHAT_ID, request).block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that delete non existing link returned CONFLICT error")
    public void testThatDeleteNonExistingLinkReturnedConflictError() {
        RemoveLinkRequest request = new RemoveLinkRequest(URI.create("https://github.com"));

        wireMockServer.stubFor(WireMock.delete(SCRAPPER_LINK_URI)
            .withHeader(TG_CHAT_ID, containing(String.valueOf(REGISTER_CHAT_ID)))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(request.link().toString())))
            .willReturn(WireMock.status(HttpStatus.CONFLICT.value())));

        ResponseEntity<Void> response = scrapperLinksClient.deleteLink(REGISTER_CHAT_ID, request).block();

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that delete link with non-registered chat returned NOT FOUND error")
    public void testThatDeleteLinkWithNonRegisterChatReturnedNotFoundError() {
        RemoveLinkRequest request = new RemoveLinkRequest(URI.create("https://github.com"));

        wireMockServer.stubFor(WireMock.delete(SCRAPPER_LINK_URI)
            .withHeader(TG_CHAT_ID, containing(String.valueOf(NON_REGISTER_CHAT_ID)))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(request.link().toString())))
            .willReturn(WireMock.notFound()));

        ResponseEntity<Void> response = scrapperLinksClient.deleteLink(NON_REGISTER_CHAT_ID, request).block();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test that delete link with registered chat returned valid response")
    public void testThatDeleteLinkWithRegisterChatReturnedValidResponse() {
        RemoveLinkRequest request = new RemoveLinkRequest(URI.create("https://github.com"));

        wireMockServer.stubFor(WireMock.delete(SCRAPPER_LINK_URI)
            .withHeader(TG_CHAT_ID, containing(String.valueOf(NON_REGISTER_CHAT_ID)))
            .withRequestBody(WireMock.equalToJson("""
                {
                    "link": "%s"
                }
                """.formatted(request.link().toString())))
            .willReturn(WireMock.ok()));

        ResponseEntity<Void> response = scrapperLinksClient.deleteLink(NON_REGISTER_CHAT_ID, request).block();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
