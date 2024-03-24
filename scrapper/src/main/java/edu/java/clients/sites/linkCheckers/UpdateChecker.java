package edu.java.clients.sites.linkCheckers;

import edu.java.repository.entity.Link;
import java.net.URI;
import reactor.core.publisher.Mono;

public abstract class UpdateChecker {
    public abstract boolean isMatched(URI link);

    public abstract Mono<String> getUpdate(Link link);
}
