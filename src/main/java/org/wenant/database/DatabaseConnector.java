package org.wenant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для установки соединения с базой данных.
 */
public class DatabaseConnector {

    /**
     * Устанавливает соединение с базой данных.
     *
     * @return Объект Connection для взаимодействия с базой данных.
     * @throws SQLException В случае ошибки при установке соединения.
     */
    public static Connection connection() throws SQLException {
        try {
            var URL = DatabaseConfig.getDbUrl();
            var USER_NAME = DatabaseConfig.getDbUsername();
            var PASSWORD = DatabaseConfig.getDbPassword();

            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

}
