package edu.java.bot.clients;

import edu.java.bot.dto.responses.ApiErrorResponse;
import edu.java.bot.exception.ApiErrorResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperChatClient {
    private final WebClient webClient;
    private static final String TG_CHAT = "scrapper/tg-chat";
    private static final String TG_CHAT_ID = "Tg-Chat-Id";

    public ScrapperChatClient(String baseUrl) {
        webClient = WebClient.create(baseUrl);
    }

    public Mono<ResponseEntity<Void>> registerChat(Long id) {
        return webClient
            .post()
            .uri(TG_CHAT).header(TG_CHAT_ID, id.toString())
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.BAD_REQUEST.equals(statusCode)
                    || HttpStatus.NOT_FOUND.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toEntity(Void.class);
    }

    public Mono<ResponseEntity<Void>> deleteChat(Long id) {
        return webClient
            .delete()
            .uri(TG_CHAT).header(TG_CHAT_ID, id.toString())
            .retrieve()
            .onStatus(
                statusCode -> HttpStatus.BAD_REQUEST.equals(statusCode)
                    || HttpStatus.NOT_FOUND.equals(statusCode),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toEntity(Void.class);
    }
}
