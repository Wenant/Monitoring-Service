package org.wenant.service.in;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.wenant.domain.entity.User;
import org.wenant.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class AuthServiceTest {

    @Test
    void authenticateUser_validCredentials_returnsAuthenticatedUser() {

        String username = "testUser";
        String password = "testPassword";
        String role = "testRole";
        User testUser = new User(username, password, role);

        UserService userServiceMock = Mockito.mock(UserService.class);
        AuthService authService = new AuthService(userServiceMock);
        Mockito.when(userServiceMock.getUserByUsername(username)).thenReturn(testUser);

        User authenticatedUser = authService.authenticateUser(username, password);
        assertEquals(testUser, authenticatedUser);
    }

    @Test
    void authenticateUser_invalidCredentials_returnsNull() {

        String username = "testUser";
        String password = "correctPassword";
        String role = "testRole";

        UserService userServiceMock = Mockito.mock(UserService.class);

        AuthService authService = new AuthService(userServiceMock);

        // Создаем тестового пользователя
        User testUser = new User(username, "incorrectPassword", role);
        Mockito.when(userServiceMock.getUserByUsername(username)).thenReturn(testUser);

        User authenticatedUser = authService.authenticateUser(username, password);
        assertNull(authenticatedUser);
    }
}