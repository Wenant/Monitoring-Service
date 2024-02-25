package org.wenant.service.in;

import org.springframework.stereotype.Service;
import org.wenant.domain.dto.RegistrationDto;
import org.wenant.domain.model.User;
import org.wenant.domain.repository.interfaces.UserRepository;
import org.wenant.mapper.UserMapper;

/**
 * Сервис регистрации новых пользователей.
 * Обеспечивает функциональность регистрации новых пользователей и проверку валидности вводимых данных.
 */
@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public RegistrationService(UserRepository userRepository,
                               UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public RegistrationResult registerUser(RegistrationDto registrationDto) {
        String username = registrationDto.getUsername();
        String password = registrationDto.getPassword();

        if (!isValidUsername(username)) {
            return RegistrationResult.INVALID_USERNAME;
        }

        if (!isValidPassword(password)) {
            return RegistrationResult.INVALID_PASSWORD;
        }

        User existingUser = userRepository.getUserByUsername(username);
        if (existingUser != null) {
            return RegistrationResult.USERNAME_ALREADY_EXISTS;
        }

        User newUser = userMapper.INSTANCE.registrationDtoToUser(registrationDto);
        userRepository.addUser(newUser);

        return RegistrationResult.SUCCESS;
    }

    /**
     * Проверяет, является ли пароль допустимым.
     *
     * @param password Пароль для проверки.
     * @return true, если пароль допустим, иначе false.
     */
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }

    /**
     * Проверяет, является ли имя пользователя допустимым.
     *
     * @param username Имя пользователя для проверки.
     * @return true, если имя пользователя допустимо, иначе false.
     */
    private boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }

    /**
     * Перечисление, представляющее возможные результаты регистрации.
     */
    public enum RegistrationResult {
        SUCCESS,
        INVALID_USERNAME,
        INVALID_PASSWORD,
        USERNAME_ALREADY_EXISTS
    }
}
