package edu.java.repository;

import edu.java.repository.entity.LinkageTable;
import java.util.List;

public interface LinkageTableRepository {
    void save(LinkageTable chatLink);

    List<LinkageTable> findByChatId(Long chatId);

    List<LinkageTable> findByLinkId(Long linkId);

    void removeByChatIdAndLinkId(Long chatId, Long linkId);

    Integer countByLinkId(Long linkId);
}
