package org.wenant.domain.entity;

import java.time.LocalDateTime;

/**
 * Класс, представляющий журнал аудита действий в системе.
 */
public class AuditLog {

    /**
     * Метка времени записи в журнале.
     */
    private final LocalDateTime timestamp;

    /**
     * Имя пользователя, выполнившего действие.
     */
    private String username;

    /**
     * Описание выполняемого действия.
     */
    private String action;

    /**
     * Конструктор класса AuditLog.
     *
     * @param username Имя пользователя.
     * @param action   Описание выполняемого действия.
     */
    public AuditLog(String username, String action) {
        this.timestamp = LocalDateTime.now();
        this.username = username;
        this.action = action;
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
     * Установить имя пользователя.
     *
     * @param username Имя пользователя.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получить описание выполняемого действия.
     *
     * @return Описание действия.
     */
    public String getAction() {
        return action;
    }

    /**
     * Установить описание выполняемого действия.
     *
     * @param action Описание действия.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Переопределение метода toString для удобного вывода информации о записи в журнале аудита.
     *
     * @return Строковое представление объекта AuditLog.
     */
    @Override
    public String toString() {
        return "AuditLog{" +
                "timestamp=" + timestamp +
                ", username='" + username + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
