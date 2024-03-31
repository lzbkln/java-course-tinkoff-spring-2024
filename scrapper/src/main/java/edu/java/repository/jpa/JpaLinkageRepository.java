package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaLink;
import edu.java.repository.jpa.entity.JpaLinkage;
import edu.java.repository.jpa.entity.JpaLinkageId;
import edu.java.repository.jpa.entity.JpaTelegramChat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkageRepository extends JpaRepository<JpaLinkage, JpaLinkageId> {
    @Query("select l from JpaLinkage l where l.chatId.id = :chatId")
    List<JpaLinkage> findByChatId(@Param("chatId") Long chatId);

    @Query("select l from JpaLinkage l where l.linkId.id = :linkId")
    List<JpaLinkage> getByLinkId(@Param("linkId") Long linkId);

    @Modifying
    @Query("delete from JpaLinkage l where l.linkId.id = :linkId and l.chatId.id = :chatId")
    void deleteByLinkIdAndChatId(@Param("linkId") Long linkId, @Param("chatId") Long chatId);

    @Query("select count(l) from JpaLinkage l where l.linkId.id = :linkId")
    Long countByLinkId(@Param("linkId") Long linkId);

    JpaLinkage getByLinkIdAndChatId(JpaLink linkId, JpaTelegramChat chatId);
}
