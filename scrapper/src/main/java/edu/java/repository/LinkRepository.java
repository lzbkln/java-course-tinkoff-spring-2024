package edu.java.repository;

import edu.java.repository.entity.Link;

public interface LinkRepository {
    void save(Link link);

    Link findById(Long id);

    Link findByUrl(String url);

    void removeById(Long id);
}
