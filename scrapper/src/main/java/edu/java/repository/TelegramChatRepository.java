package edu.java.repository;

import edu.java.repository.entity.TelegramChat;

public interface TelegramChatRepository {

    void saveUser(TelegramChat user);

    TelegramChat findById(Long id);

    void deleteById(Long id);
}
