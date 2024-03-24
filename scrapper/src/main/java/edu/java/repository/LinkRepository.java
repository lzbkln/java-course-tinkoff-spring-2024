package edu.java.repository;

import edu.java.repository.entity.Link;
import java.util.List;

public interface LinkRepository {
    void save(Link link);

    Link findById(Long id);

    Link findByUrl(String url);

    boolean findByUrlBool(String url);

    void removeById(Long id);

    void updateLink(Link link);

    List<Link> findLinksToUpdate();
}
