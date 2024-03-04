package org.wenant.domain.model;

import org.wenant.starter.domain.audit.UserData;

/**
 * Класс представляет сущность пользователя.
 */
public class User implements UserData {
    private final Long id;
    private final Role role;
    private final String username;
    private final String password;

    /**
     * Конструктор для создания объекта пользователя с указанными параметрами.
     *
     * @param id       Уникальный идентификатор пользователя.
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @param role     Роль пользователя.
     */
    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Получить уникальный идентификатор пользователя.
     *
     * @return Уникальный идентификатор пользователя.
     */
    public Long getId() {
        return id;
    }

    /**
     * Получить имя пользователя.
     *
     * @return Имя пользователя.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Получить пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Получить роль пользователя.
     *
     * @return Роль пользователя.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Возвращает строковое представление объекта пользователя.
     *
     * @return Строковое представление объекта пользователя.
     */
    @Override
    public String toString() {
        return "\nИмя пользователя: " + username + ", Роль: " + role;
    }

    /**
     * Перечисление представляет роли пользователя.
     */
    public enum Role {
        /**
         * Роль администратора.
         */
        ADMIN,

        /**
         * Роль пользователя.
         */
        USER
    }
}
