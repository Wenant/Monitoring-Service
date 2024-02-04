package org.wenant.service.in;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wenant.domain.entity.User;
import org.wenant.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void whenAuthenticateUserWithValidCredentials_thenReturnUser() {
        String username = "user1";
        String password = "password123";
        User user = new User(1L, username, password, User.Role.USER);
        when(userService.getUserByUsername(username)).thenReturn(user);

        User result = authService.authenticateUser(username, password);

        verify(userService, times(1)).getUserByUsername(username);
        assertEquals(user, result);
    }

    @Test
    public void whenAuthenticateUserWithInvalidCredentials_thenReturnNull() {
        String username = "user1";
        String password = "invalidPassword";
        User user = new User(1L, username, "correctPassword", User.Role.USER);
        when(userService.getUserByUsername(username)).thenReturn(user);

        User result = authService.authenticateUser(username, password);

        verify(userService, times(1)).getUserByUsername(username);
        assertNull(result);
    }

    @Test
    public void whenAuthenticateUserWithNonExistingUsername_thenReturnNull() {
        String nonExistingUsername = "nonExistingUser";
        when(userService.getUserByUsername(nonExistingUsername)).thenReturn(null);

        User result = authService.authenticateUser(nonExistingUsername, "password123");

        verify(userService, times(1)).getUserByUsername(nonExistingUsername);
        assertNull(result);
    }
}
