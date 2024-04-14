package edu.java.clients.sites;

import edu.java.dto.responses.StackOverflowResponseDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class StackOverflowClient {
    private final WebClient client;
    private final Retry retry;

    public StackOverflowClient(String url, Retry retry) {
        client = WebClient.create(url);
        this.retry = retry;
    }

    public Mono<StackOverflowResponseDTO> getQuestionsInfo(String id) {
        return client.get()
            .uri("/questions/" + id + "?site=stackoverflow")
            .retrieve()
            .bodyToMono(StackOverflowResponseDTO.class)
            .retryWhen(retry);
    }
}
