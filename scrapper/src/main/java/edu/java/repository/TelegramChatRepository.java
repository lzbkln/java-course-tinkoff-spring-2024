package edu.java.repository;

import edu.java.repository.entity.TelegramChat;
import java.util.Optional;

public interface TelegramChatRepository {

    void saveUser(TelegramChat user);

    Optional<TelegramChat> findById(Long id);

    boolean deleteById(Long id);
}
