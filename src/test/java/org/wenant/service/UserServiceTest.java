package org.wenant.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void whenGetAllUsers_thenReturnAllUsers() {
        List<User> users = List.of(new User(1l, "user1", "qeqw", User.Role.ADMIN),
                new User(2l, "user2", "qeqw", User.Role.USER));
        when(userRepository.getAllUsers()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        verify(userRepository, times(1)).getAllUsers();
        assertEquals(users, result);
    }

    @Test
    public void whenGetUserByUsername_thenReturnUser() {
        User user = new User(1l, "user1", "qeqw", User.Role.ADMIN);
        when(userRepository.getUserByUsername("user1")).thenReturn(user);

        User result = userService.getUserByUsername("user1");

        verify(userRepository, times(1)).getUserByUsername("user1");
        assertEquals(user, result);
    }

    @Test
    public void whenGetUserByNonExistingUsername_thenReturnNull() {// given
        String nonExistingUsername = "nonExistingUser";
        when(userRepository.getUserByUsername(nonExistingUsername)).thenReturn(null);

        User result = userService.getUserByUsername(nonExistingUsername);

        verify(userRepository, times(1)).getUserByUsername(nonExistingUsername);
        assertNull(result);
    }
}