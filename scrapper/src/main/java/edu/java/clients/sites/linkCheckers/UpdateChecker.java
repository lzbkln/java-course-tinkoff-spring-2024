package edu.java.clients.sites.linkCheckers;

import edu.java.repository.jpa.entity.CommonLink;
import java.net.URI;
import reactor.core.publisher.Mono;

public interface UpdateChecker {
    boolean isMatched(URI link);

    Mono<String> getUpdate(CommonLink link);
}
