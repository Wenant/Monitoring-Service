package org.wenant.service;

import org.wenant.domain.dto.UserDto;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.UserRepository;
import org.wenant.mapper.UserMapper;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 */
public class UserService {

    private final UserRepository userRepository;

    /**
     * Конструктор.
     *
     * @param userRepository Репозиторий пользователей.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей.
     */
    public List<UserDto> getAllUsers() {
        return UserMapper.INSTANCE.userListToUserDtoList(userRepository.getAllUsers());
    }

    /**
     * Получает пользователя по его имени.
     *
     * @param username Имя пользователя.
     * @return Пользователь с указанным именем или null, если такого пользователя нет.
     */
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}
