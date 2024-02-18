package org.wenant.service.in;

import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.UserRepository;
import org.wenant.mapper.UserMapper;

/**
 * Сервис аутентификации пользователей.
 * Обеспечивает проверку и подтверждение подлинности пользователя.
 */
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public AuthenticationDto authenticateUser(String username, String password) {
        User user = userRepository.getUserByUsername(username);
        AuthenticationDto authenticationDto = UserMapper.INSTANCE.userToAuthenticationDto(user);

        if (user == null || !password.equals(user.getPassword())) {
            return null;
        } else {
            return authenticationDto;
        }
    }
}
