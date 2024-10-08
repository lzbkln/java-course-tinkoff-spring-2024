package edu.java.bot.clients;

import edu.java.bot.dto.responses.ApiErrorResponse;
import edu.java.bot.exception.ApiErrorResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ScrapperChatClient {
    private final WebClient webClient;
    private final Retry retry;
    private static final String TG_CHAT = "/scrapper/tg-chat/{id}";

    public ScrapperChatClient(String baseUrl, Retry retry) {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
        this.retry = retry;
    }

    public Mono<ResponseEntity<Void>> registerChat(Long id) {
        return webClient
            .post()
            .uri(TG_CHAT, id)
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.NOT_FOUND.equals(statusCode)
                    || HttpStatus.CONFLICT.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(ApiErrorResponseException::new)
                    .flatMap(Mono::error)
            )
            .toBodilessEntity()
            .retryWhen(retry);
    }

    public Mono<ResponseEntity<Void>> deleteChat(Long id) {
        return webClient
            .delete()
            .uri(TG_CHAT, id)
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.NOT_FOUND.equals(statusCode)
                    || HttpStatus.CONFLICT.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(ApiErrorResponseException::new)
                    .flatMap(Mono::error)
            )
            .toBodilessEntity()
            .retryWhen(retry);
    }
}
