package edu.java.bot.repository;

import edu.java.bot.BotApplication;
import edu.java.bot.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public class UserRepositoryTest {
    private UserRepository userRepository;
    @Mock
    private User mockUser;
    @Mock
    private User updatedUser;

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    @DisplayName("Test saving a user")
    public void testSaveUser() {
        when(mockUser.getId()).thenReturn(1L);
        userRepository.saveUser(mockUser);

        assertEquals(1, userRepository.getDbUsers().size());
        assertEquals(mockUser, userRepository.getDbUsers().get(1L));
    }

    @Test
    @DisplayName("Test finding a user by ID")
    public void testFindUserById() {
        when(mockUser.getId()).thenReturn(1L);
        userRepository.getDbUsers().put(1L, mockUser);

        assertEquals(mockUser, userRepository.findById(1L));
        assertNull(userRepository.findById(2L));
    }

    @Test
    @DisplayName("Test updating a user by ID")
    public void testUpdateUserById() {
        when(updatedUser.getId()).thenReturn(1L);
        userRepository.getDbUsers().put(1L, mockUser);
        userRepository.updateUserById(1L, updatedUser);

        assertEquals(updatedUser, userRepository.getDbUsers().get(1L));
    }
}

