package org.wenant.service.in;

import org.wenant.domain.entity.User;
import org.wenant.domain.repository.UserRepository;

/**
 * Сервис регистрации новых пользователей.
 * Обеспечивает функциональность регистрации новых пользователей и проверку валидности вводимых данных.
 */
public class RegistrationService {

    private final UserRepository userRepository;

    /**
     * Конструктор класса RegistrationService.
     *
     * @param userRepository Репозиторий пользователей для взаимодействия с хранилищем данных о пользователях.
     */
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param username Имя пользователя для регистрации.
     * @param password Пароль для регистрации.
     * @return Результат регистрации (успешно, некорректное имя пользователя, некорректный пароль, имя пользователя уже существует).
     * @implNote При успешной регистрации устанавливается роль "user".
     */
    public RegistrationResult registerUser(String username, String password) {
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

        User newUser = new User(username, password, "user"); // Роль "user" при регистрации
        userRepository.addUser(newUser);

        return RegistrationResult.SUCCESS;
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }

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
