package edu.java.service.jdbc;

import edu.java.repository.LinkRepository;
import edu.java.repository.LinkageRepository;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.Linkage;
import edu.java.service.LinkUpdater;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkUpdaterService implements LinkUpdater {
    private final LinkRepository linkRepository;
    private final LinkageRepository linkageTableRepository;

    @Override
    @Transactional
    public void update(Link link) {
        linkRepository.updateLink(link);
    }

    @Override
    @Transactional
    public List<Link> findLinksToUpdate() {
        return linkRepository.findLinksToUpdate();
    }

    @Override
    @Transactional
    public List<Long> findTgChatIds(Long linkId) {
        return linkageTableRepository
            .findByLinkId(linkId)
            .stream()
            .map(Linkage::getChatId)
            .collect(Collectors.toList());
    }
}
