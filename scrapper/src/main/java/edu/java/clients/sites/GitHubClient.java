package edu.java.clients.sites;

import edu.java.dto.responses.GitHubResponseDTO;
import edu.java.dto.responses.GithubBranchResponseDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class GitHubClient {
    private final WebClient client;
    private static final String REPOS = "/repos/";
    private final Retry retry;

    public GitHubClient(String url, Retry retry) {
        client = WebClient.create(url);
        this.retry = retry;
    }

    public Mono<GitHubResponseDTO> getUserRepository(String repositoryPath) {
        return client.get()
            .uri(REPOS + repositoryPath)
            .retrieve()
            .bodyToMono(GitHubResponseDTO.class)
            .retryWhen(retry);
    }

    public Mono<GithubBranchResponseDTO[]> getBranchesFromUserRepository(String repositoryPath) {
        return client.get()
            .uri(REPOS + repositoryPath + "/branches")
            .retrieve()
            .bodyToMono(GithubBranchResponseDTO[].class)
            .retryWhen(retry);
    }
}
