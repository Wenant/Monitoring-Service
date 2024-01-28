package org.wenant.service.in;

import org.wenant.domain.entity.User;
import org.wenant.service.UserService;

/**
 * Сервис аутентификации пользователей.
 * Обеспечивает проверку и подтверждение подлинности пользователя.
 */
public class AuthService {

    private final UserService userService;

    /**
     * Конструктор класса AuthService.
     *
     * @param userService Сервис пользователей, необходимый для проверки аутентификации.
     */
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Проверяет аутентификацию пользователя по его имени пользователя и паролю.
     *
     * @param username Имя пользователя, которое нужно проверить.
     * @param password Пароль пользователя, который нужно проверить.
     * @return Объект User, представляющий аутентифицированного пользователя, или null, если аутентификация не удалась.
     */
    public User authenticateUser(String username, String password) {
        User user = userService.getUserByUsername(username);

        if (user == null || !password.equals(user.getPassword())) {
            return null;
        } else {
            return user;
        }
    }
}
