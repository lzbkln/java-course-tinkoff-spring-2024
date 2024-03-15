package edu.java.repository;

import edu.java.repository.entity.LinkageTable;
import java.util.List;

public interface LinkageTableRepository {
    void save(LinkageTable chatLink);

    List<LinkageTable> findByChatId(long chatId);

    List<LinkageTable> findByLinkId(long linkId);

    void removeByChatIdAndLinkId(long chatId, long linkId);
}
