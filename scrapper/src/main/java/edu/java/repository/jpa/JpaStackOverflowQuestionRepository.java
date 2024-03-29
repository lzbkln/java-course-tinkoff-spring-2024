package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaLink;
import edu.java.repository.jpa.entity.JpaStackOverflowQuestion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStackOverflowQuestionRepository extends JpaRepository<JpaStackOverflowQuestion, Long> {
    @Modifying
    void deleteByLinkId(JpaLink linkId);

    Optional<JpaStackOverflowQuestion> findByLinkId(JpaLink linkId);
}
