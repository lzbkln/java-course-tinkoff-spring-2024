package edu.java.clients.sites.linkCheckers;

import edu.java.clients.sites.GitHubClient;
import edu.java.clients.sites.util.Utils;
import edu.java.dto.responses.GithubBranchResponseDTO;
import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.entity.GithubBranches;
import edu.java.repository.entity.Link;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class GitHubUpdateChecker extends UpdateChecker {
    private final GitHubClient gitHubClient;
    private final Utils githubUtil;
    private final GithubBranchesRepository githubBranchesRepository;

    public Mono<String> getUpdate(Link link) {
        String path = githubUtil.extractPathForGithub(link.getUrl());
        return gitHubClient.getUserRepository(path)
            .flatMap(response -> {
                if (response.pushedAt().isAfter(link.getLastUpdatedAt())) {
                    return githubUtil.getBranches(link.getUrl())
                        .mapNotNull(newResponse -> {
                            Set<String> newBranches =
                                Arrays.stream(newResponse).map(GithubBranchResponseDTO::name).collect(
                                    Collectors.toSet());
                            Set<String> tempBranches = new HashSet<>(newBranches);
                            Set<String> oldBranches = githubBranchesRepository.findByLinkId(link.getId()).getBranches();
                            tempBranches.removeAll(oldBranches);
                            if (!tempBranches.isEmpty()) {
                                githubBranchesRepository.updateData(new GithubBranches(
                                    link.getId(),
                                    newBranches
                                ));
                                return "Добавление новыx веток по ссылке: %s. Добавлены: ".formatted(link.getUrl())
                                    + String.join(", ", tempBranches);
                            }
                            return null;
                        });
                }
                return Mono.empty();
            });
    }

    public boolean isMatched(URI uri) {
        return uri.getHost().equals("github.com");
    }
}
