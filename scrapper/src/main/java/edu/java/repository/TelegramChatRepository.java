package edu.java.repository;

import edu.java.entity.TelegramChat;

public interface TelegramChatRepository {

    void saveUser(TelegramChat user);

    TelegramChat findById(Long id);

    void updateUserById(Long id, TelegramChat updatedUser);

    void deleteById(Long id);
}
