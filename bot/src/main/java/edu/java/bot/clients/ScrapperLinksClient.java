package edu.java.bot.clients;

import edu.java.bot.dto.requests.AddLinkRequest;
import edu.java.bot.dto.requests.RemoveLinkRequest;
import edu.java.bot.dto.responses.ApiErrorResponse;
import edu.java.bot.dto.responses.ListLinksResponse;
import edu.java.bot.exception.ApiErrorResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperLinksClient {
    private static final String TG_CHAT_ID = "Tg-Chat-Id";
    private static final String LINKS = "scrapper/links";
    private final WebClient webClient;

    public ScrapperLinksClient(String baseUrl) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public Mono<ResponseEntity<ListLinksResponse>> getAllLinks(Long id) {
        return webClient.get()
            .uri(LINKS).header(TG_CHAT_ID, id.toString())
            .retrieve()
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toEntity(ListLinksResponse.class);
    }

    public Mono<ResponseEntity<Void>> addLink(Long id, AddLinkRequest request) {
        return webClient.post()
            .uri(LINKS).header(TG_CHAT_ID, id.toString())
            .bodyValue(request)
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.NOT_FOUND.equals(statusCode)
                    || HttpStatus.CONFLICT.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toBodilessEntity();
    }

    public Mono<ResponseEntity<Void>> deleteLink(Long id, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS).header(TG_CHAT_ID, id.toString())
            .bodyValue(request)
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.NOT_FOUND.equals(statusCode)
                    || HttpStatus.CONFLICT.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toBodilessEntity();
    }
}
