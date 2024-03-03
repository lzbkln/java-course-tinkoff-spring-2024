package edu.java.repository;

import edu.java.entity.Link;
import java.net.URI;
import java.util.Optional;

public interface LinkRepository {
    Link save(Link link);

    Optional<Link> findByUrl(URI url);
}
