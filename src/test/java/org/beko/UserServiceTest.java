package org.beko;

import org.beko.model.User;
import org.beko.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @Test
    public void testRegisterUser() {
        User user = userService.register("testUser", "password");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        userService.register("testUser", "password");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register("testUser", "password");
        });
        assertEquals("User already exists.", exception.getMessage());
    }

    @Test
    public void testLoginUserSuccessfully() {
        userService.register("testUser", "password");
        User user = userService.login("testUser", "password");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testLoginUserInvalidCredentials() {
        userService.register("testUser", "password");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login("testUser", "wrongPassword");
        });
        assertEquals("Invalid username or password.", exception.getMessage());
    }

    @Test
    public void testHasUser() {
        userService.register("testUser", "password");
        assertTrue(userService.hasUser("testUser"));
        assertFalse(userService.hasUser("nonExistentUser"));
    }
}
