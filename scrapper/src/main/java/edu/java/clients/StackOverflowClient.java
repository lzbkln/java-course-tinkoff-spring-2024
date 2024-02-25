package edu.java.clients;

import edu.java.dto.StackOverflowResponseDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {
    protected final WebClient client;

    public StackOverflowClient(String url) {
        client = WebClient.create(url);
    }

    public Mono<StackOverflowResponseDTO> getQuestionsInfo(String ids) {
        return client.get()
            .uri("/questions/" + ids + "?site=stackoverflow")
            .retrieve()
            .bodyToMono(StackOverflowResponseDTO.class);
    }
}
