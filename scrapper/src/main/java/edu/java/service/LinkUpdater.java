package edu.java.service;

import edu.java.repository.entity.Link;
import java.util.List;

public interface LinkUpdater {
    void update(Link link);

    List<Link> findLinksToUpdate();

    List<Long> findTgChatIds(Long linkId);
}
