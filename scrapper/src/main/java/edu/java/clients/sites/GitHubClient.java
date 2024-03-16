package edu.java.clients.sites;

import edu.java.dto.responses.GitHubResponseDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubClient {
    private final WebClient client;

    public GitHubClient(String url) {
        client = WebClient.create(url);
    }

    public Mono<GitHubResponseDTO> getUserRepository(String repositoryPath) {
        return client.get()
            .uri("/repos/" + repositoryPath)
            .retrieve()
            .bodyToMono(GitHubResponseDTO.class);
    }
}
