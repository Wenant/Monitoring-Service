package org.wenant.service;

import org.springframework.stereotype.Service;
import org.wenant.domain.dto.UserDto;
import org.wenant.domain.model.User;
import org.wenant.domain.repository.interfaces.UserRepository;
import org.wenant.mapper.UserMapper;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Конструктор.
     *
     * @param userRepository Репозиторий пользователей.
     */
    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей.
     */
    public List<UserDto> getAllUsers() {
        return userMapper.userListToUserDtoList(userRepository.getAllUsers());
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
