package edu.java.clients;

import edu.java.dto.responses.StackOverflowResponseDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {
    protected final WebClient client;

    public StackOverflowClient(String url) {
        client = WebClient.create(url);
    }

    public Mono<StackOverflowResponseDTO> getQuestionsInfo(String id) {
        return client.get()
            .uri("/questions/" + id + "?site=stackoverflow")
            .retrieve()
            .bodyToMono(StackOverflowResponseDTO.class);
    }
}
