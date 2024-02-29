package edu.java.bot.repository;

import edu.java.bot.entities.User;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class UserRepository {
    private final Map<Long, User> dbUsers = new HashMap<>();

    public void saveUser(User user) {
        dbUsers.put(user.getId(), user);
    }

    public User findById(Long id) {
        return dbUsers.get(id);
    }

    public void updateUserById(Long id, User updatedUser) {
        dbUsers.put(id, updatedUser);
    }
}
