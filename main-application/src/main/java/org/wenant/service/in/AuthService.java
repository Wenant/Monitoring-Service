package org.wenant.service.in;

import org.springframework.stereotype.Service;
import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.domain.model.User;
import org.wenant.domain.repository.interfaces.UserRepository;
import org.wenant.mapper.UserMapper;

import java.util.Optional;

/**
 * Сервис аутентификации пользователей.
 * Обеспечивает проверку и подтверждение подлинности пользователя.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public Optional<AuthenticationDto> authenticateUser(AuthenticationDto dtoFromRequest) {
        User user = userRepository.getUserByUsername(dtoFromRequest.getUsername());

        if (user == null || !dtoFromRequest.getPassword().equals(user.getPassword())) {
            return Optional.empty();
        } else {
            AuthenticationDto authenticationDto = userMapper.userToAuthenticationDto(user);
            return Optional.of(authenticationDto);
        }
    }
}