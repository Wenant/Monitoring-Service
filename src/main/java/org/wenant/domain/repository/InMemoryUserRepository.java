package org.wenant.domain.repository;

import org.wenant.domain.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация UserRepository, хранящая пользователей в памяти.
 */
public class InMemoryUserRepository implements UserRepository {

    private final List<User> users;

    /**
     * Создает новый объект InMemoryUserRepository и добавляет администратора по умолчанию.
     */
    public InMemoryUserRepository() {
        this.users = new ArrayList<>();
        User adminUser = new User("admin", "admin", "admin");
        users.add(adminUser);
    }

    /**
     * Добавляет пользователя в репозиторий.
     *
     * @param user Пользователь для добавления.
     */
    @Override
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Получает список всех пользователей из репозитория.
     *
     * @return Список всех пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Получает пользователя по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Пользователь с указанным именем пользователя или null, если пользователь не найден.
     */
    @Override
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
