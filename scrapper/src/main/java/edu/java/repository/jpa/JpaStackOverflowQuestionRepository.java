package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaStackOverflowQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStackOverflowQuestionRepository extends JpaRepository<JpaStackOverflowQuestion, Long> {
    @Query("select l from JpaStackOverflowQuestion l where l.linkId.id = :linkId")
    JpaStackOverflowQuestion findByLinkId(@Param("linkId") Long linkId);

    @Modifying
    @Query("update JpaStackOverflowQuestion g set g.answerCount = :answerCount where g.linkId.id = :linkId")
    void updateData(@Param("linkId") Long linkId, @Param("answerCount") int answerCount);

    @Modifying
    @Query("delete from JpaStackOverflowQuestion s where s.linkId.id = :linkId")
    void removeByLinkId(@Param("linkId") Long linkId);
}
