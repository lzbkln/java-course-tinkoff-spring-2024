package edu.java.clients.sites.linkCheckers;

import edu.java.clients.sites.GitHubClient;
import edu.java.repository.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class GitHubUpdateChecker extends UpdateChecker {
    private final GitHubClient gitHubClient;

    public Mono<String> getUpdate(Link link) {
        return gitHubClient.getUserRepository(link.getUrl())
            .mapNotNull(response -> {
                if (response.updatedAt().isAfter(OffsetDateTime.from(link.getLastUpdatedAt()))) {
                    return "Обновление по ссылке %s".formatted(link.getUrl());
                }
                return null;
            });
    }

    public boolean isMatched(URI uri) {
        return uri.getHost().equals("github.com");
    }
}
