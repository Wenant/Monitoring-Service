package org.wenant.domain.entity;

/**
 * Класс представляет сущность пользователя.
 */
public class User {

    private final String role;
    private final String username;
    private final String password;

    /**
     * Конструктор для создания объекта пользователя с указанными параметрами.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @param role     Роль пользователя.
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Получает имя пользователя.
     *
     * @return Имя пользователя.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Получает пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Получает роль пользователя.
     *
     * @return Роль пользователя.
     */
    public String getRole() {
        return role;
    }

    /**
     * Возвращает строковое представление объекта пользователя.
     *
     * @return Строковое представление объекта пользователя.
     */
    @Override
    public String toString() {
        return "\n Имя пользователя: " + username + ", Роль: " + role;
    }
}
