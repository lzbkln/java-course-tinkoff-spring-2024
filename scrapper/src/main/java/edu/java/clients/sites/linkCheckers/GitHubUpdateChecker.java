package edu.java.clients.sites.linkCheckers;

import edu.java.clients.sites.GitHubClient;
import edu.java.repository.entity.Link;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class GitHubUpdateChecker extends UpdateChecker {
    private final GitHubClient gitHubClient;

    public Mono<String> getUpdate(Link link) {
        String path = extractPath(link.getUrl());
        return gitHubClient.getUserRepository(path)
            .mapNotNull(response -> {
                if (response.pushedAt().isAfter(link.getLastUpdatedAt())) {
                    return "Обновление по ссылке %s".formatted(link.getUrl());
                }
                return "null";
            });
    }

    public boolean isMatched(URI uri) {
        return uri.getHost().equals("github.com");
    }

    private String extractPath(String url) {
        String regex = "https?://github.com/([^/]+)/([^/]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            return matcher.group(1) + "/" + matcher.group(2);
        }
        return null;
    }
}
