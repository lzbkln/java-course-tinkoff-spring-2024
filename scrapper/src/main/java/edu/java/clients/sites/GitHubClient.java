package edu.java.clients.sites;

import edu.java.dto.responses.GitHubResponseDTO;
import edu.java.dto.responses.GithubBranchResponseDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubClient {
    private final WebClient client;
    private static final String REPOS = "/repos/";

    public GitHubClient(String url) {
        client = WebClient.create(url);
    }

    public Mono<GitHubResponseDTO> getUserRepository(String repositoryPath) {
        return client.get()
            .uri(REPOS + repositoryPath)
            .retrieve()
            .bodyToMono(GitHubResponseDTO.class);
    }

    public Mono<GithubBranchResponseDTO[]> getBranchesFromUserRepository(String repositoryPath) {
        return client.get()
            .uri(REPOS + repositoryPath + "/branches")
            .retrieve()
            .bodyToMono(GithubBranchResponseDTO[].class);
    }
}
