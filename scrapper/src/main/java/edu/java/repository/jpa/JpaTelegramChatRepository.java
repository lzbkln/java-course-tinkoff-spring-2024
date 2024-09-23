package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaTelegramChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTelegramChatRepository extends JpaRepository<JpaTelegramChat, Long> {
}
