package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaTelegramChat;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTelegramChatRepository extends JpaRepository<JpaTelegramChat, Long> {
    @Modifying
    void deleteById(@NotNull Long id);

    @NotNull Optional<JpaTelegramChat> findById(@NotNull Long id);
}
