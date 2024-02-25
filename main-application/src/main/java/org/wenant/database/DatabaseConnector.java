package org.wenant.database;

import org.wenant.config.YamlReader;

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
            Class.forName(YamlReader.getDbDriver());
            var url = YamlReader.getDbUrl();
            var username = YamlReader.getDbUsername();
            var password = YamlReader.getDbPassword();

            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}