package edu.java.repository;

import edu.java.entity.Link;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepositoryImpl implements LinkRepository {
    private final Map<Long, Link> repository = new HashMap<>();

    @Override
    public Link save(Link link) {
        repository.put(link.id(), link);
        return link;
    }

    @Override
    public Optional<Link> findByUrl(URI url) {
        for (Link link : repository.values()) {
            if (link.url().equals(url)) {
                return Optional.of(link);
            }
        }
        return Optional.empty();
    }
}
