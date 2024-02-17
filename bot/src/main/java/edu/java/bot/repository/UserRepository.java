package edu.java.bot.repository;

import edu.java.bot.entities.User;
import java.util.HashMap;
import java.util.Objects;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public HashMap<Long, User> dbUsers = new HashMap<>();

    public void saveUser(User user) {
        dbUsers.put(user.getId(), user);
    }

    public User findById(Long id) {
        for (Long idUser : dbUsers.keySet()) {
            if (Objects.equals(id, idUser)) {
                return dbUsers.get(idUser);
            }
        }
        return null;
    }

    public void updateUserById(Long id, User updatedUser) {
        dbUsers.put(id, updatedUser);
    }
}
