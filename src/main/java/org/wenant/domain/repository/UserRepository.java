package org.wenant.domain.repository;

import org.wenant.domain.entity.User;

import java.util.List;

/**
 * Интерфейс для работы с пользователями.
 */
public interface UserRepository {

    /**
     * Добавляет нового пользователя.
     *
     * @param user Пользователь для добавления.
     */
    void addUser(User user);

    /**
     * Получает список всех пользователей.
     *
     * @return Список всех пользователей.
     */
    List<User> getAllUsers();

    /**
     * Получает пользователя по его имени.
     *
     * @param username Имя пользователя.
     * @return Объект пользователя, если найден, иначе null.
     */
    User getUserByUsername(String username);

}
