package edu.java.bot.clients;

import edu.java.bot.dto.requests.AddLinkRequest;
import edu.java.bot.dto.requests.RemoveLinkRequest;
import edu.java.bot.dto.responses.ApiErrorResponse;
import edu.java.bot.dto.responses.LinkResponse;
import edu.java.bot.dto.responses.ListLinksResponse;
import edu.java.bot.exception.ApiErrorResponseException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperLinksClient {
    private static final String TG_CHAT_ID = "Tg-Chat-Id";
    private static final String LINKS = "scrapper/links";
    private final WebClient webClient;

    public ScrapperLinksClient(String baseUrl) {
        webClient = WebClient.create(baseUrl);
    }

    public Mono<ResponseEntity<ListLinksResponse>> getAllLinks(Long id) {
        return webClient.get()
            .uri(LINKS).header(TG_CHAT_ID, id.toString())
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.NOT_FOUND.equals(statusCode)
                    || HttpStatus.BAD_REQUEST.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toEntity(ListLinksResponse.class);
    }

    public Mono<ResponseEntity<LinkResponse>> addLink(Long id, AddLinkRequest request) {
        return webClient.post()
            .uri(LINKS).header(TG_CHAT_ID, String.valueOf(id))
            .bodyValue(request)
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.NOT_FOUND.equals(statusCode)
                    || HttpStatus.BAD_REQUEST.equals(statusCode)
                    || HttpStatus.CONFLICT.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toEntity(LinkResponse.class);
    }

    public Mono<ResponseEntity<LinkResponse>> deleteLink(Long id, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS).header(TG_CHAT_ID, id.toString())
            .bodyValue(request)
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.NOT_FOUND.equals(statusCode)
                    || HttpStatus.BAD_REQUEST.equals(statusCode)
                    || HttpStatus.CONFLICT.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toEntity(LinkResponse.class);
    }
}
