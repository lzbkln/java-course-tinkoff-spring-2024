package edu.java.repository;

import edu.java.repository.entity.Linkage;
import java.util.List;

public interface LinkageRepository {
    void save(Linkage linkage);

    List<Linkage> findByChatId(Long chatId);

    List<Linkage> findByLinkId(Long linkId);

    void removeByChatIdAndLinkId(Long chatId, Long linkId);

    Integer countByLinkId(Long linkId);
}
