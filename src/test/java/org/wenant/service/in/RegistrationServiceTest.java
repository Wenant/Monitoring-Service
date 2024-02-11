package org.wenant.service.in;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.UserRepository;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    public void testRegisterUser_Success() {
        String username = "newUser";
        String password = "newPassword";
        when(userRepository.getUserByUsername(username)).thenReturn(null);

        RegistrationService.RegistrationResult result = registrationService.registerUser(username, password);

        verify(userRepository, times(1)).getUserByUsername(username);
        verify(userRepository, times(1)).addUser(any(User.class));
        assertEquals(RegistrationService.RegistrationResult.SUCCESS, result);
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        // given
        String username = "x";
        String password = "password";

        // when
        RegistrationService.RegistrationResult result = registrationService.registerUser(username, password);

        // then
        assertEquals(RegistrationService.RegistrationResult.INVALID_USERNAME, result);
        verify(userRepository, never()).getUserByUsername(anyString());
        verify(userRepository, never()).addUser(any(User.class));
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        String username = "newUser";
        String password = "x";

        RegistrationService.RegistrationResult result = registrationService.registerUser(username, password);

        assertEquals(RegistrationService.RegistrationResult.INVALID_PASSWORD, result);
        verify(userRepository, never()).getUserByUsername(anyString());
        verify(userRepository, never()).addUser(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() {
        String username = "existingUser";
        String password = "newPassword";
        when(userRepository.getUserByUsername(username)).thenReturn(new User(1L, username, "existingPassword", User.Role.USER));

        RegistrationService.RegistrationResult result = registrationService.registerUser(username, password);

        assertEquals(RegistrationService.RegistrationResult.USERNAME_ALREADY_EXISTS, result);
        verify(userRepository, times(1)).getUserByUsername(username);
        verify(userRepository, never()).addUser(any(User.class));
    }
}
