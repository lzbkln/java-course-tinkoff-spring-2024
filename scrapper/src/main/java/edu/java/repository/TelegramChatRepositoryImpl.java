package edu.java.repository;

import edu.java.entity.TelegramChat;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class TelegramChatRepositoryImpl implements TelegramChatRepository {
    private final Map<Long, TelegramChat> dbUsers = new HashMap<>();

    @Override
    public void saveUser(TelegramChat user) {
        dbUsers.put(user.getId(), user);
    }

    @Override
    public TelegramChat findById(Long id) {
        return dbUsers.get(id);
    }

    @Override
    public void updateUserById(Long id, TelegramChat updatedUser) {
        dbUsers.put(id, updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        dbUsers.remove(findById(id));
    }
}
