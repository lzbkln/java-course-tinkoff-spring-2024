package edu.java.repository;

import edu.java.repository.entity.TelegramChat;

public interface TelegramChatRepository {

    void saveUser(TelegramChat user);

    TelegramChat getById(Long id);

    void deleteById(Long id);
}
