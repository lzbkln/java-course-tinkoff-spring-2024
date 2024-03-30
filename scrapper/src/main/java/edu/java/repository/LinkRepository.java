package edu.java.repository;

import edu.java.repository.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    void save(Link link);

    Link findById(Long id);

    Optional<Link> findByUrl(String url);

    boolean findByUrlBool(String url);

    void removeById(Long id);

    void updateLink(Link link);

    List<Link> findByLastUpdatedAtBefore(OffsetDateTime time);
}
