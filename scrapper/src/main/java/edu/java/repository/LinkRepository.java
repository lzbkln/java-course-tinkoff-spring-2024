package edu.java.repository;

import edu.java.repository.entity.Link;
import java.util.Optional;

public interface LinkRepository {
    void save(Link link);

    Optional<Link> findById(Long id);

    Optional<Link> findByUrl(String url);

    boolean removeById(Long id);
}
