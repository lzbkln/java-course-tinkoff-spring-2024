package edu.java.clients.bot;

import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.dto.responses.ApiErrorResponse;
import edu.java.exception.ApiErrorResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class BotClient {
    private final WebClient webClient;
    private final Retry retry;

    public BotClient(String baseUrl, Retry retry) {
        webClient = WebClient.create(baseUrl);
        this.retry = retry;
    }

    public Mono<ResponseEntity<Void>> sendUpdate(LinkUpdateRequest request) {
        return webClient.post()
            .uri("/bot/update")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(ApiErrorResponseException::new)
                    .flatMap(Mono::error)
            )
            .toEntity(Void.class)
            .retryWhen(retry);
    }
}
