package edu.java.repository;

import edu.java.repository.entity.Linkage;
import java.util.List;

public interface LinkageRepository {
    void save(Linkage linkage);

    List<Linkage> getByChatId(Long chatId);

    List<Linkage> getByLinkId(Long linkId);

    void removeByChatIdAndLinkId(Long chatId, Long linkId);

    Integer countByLinkId(Long linkId);

    boolean findByLinkIdAndChatId(Long linkId, Long chatId);
}
