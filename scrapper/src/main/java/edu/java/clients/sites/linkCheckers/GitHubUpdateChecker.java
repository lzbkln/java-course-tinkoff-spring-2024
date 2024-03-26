package edu.java.clients.sites.linkCheckers;

import edu.java.repository.jpa.entity.CommonLink;
import edu.java.service.LinkUpdater;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class GitHubUpdateChecker extends UpdateChecker {
    private final LinkUpdater linkUpdater;

    public Mono<String> getUpdate(CommonLink link) {
        return linkUpdater.getUpdateForGithub(link);
    }

    public boolean isMatched(URI uri) {
        return uri.getHost().equals("github.com");
    }
}
