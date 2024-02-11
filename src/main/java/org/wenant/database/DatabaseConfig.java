package org.wenant.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для чтения конфигураций базы данных из файла application.properties.
 */
public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.out.println("Не удалось найти application.properties");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получает URL базы данных из конфигураций.
     *
     * @return URL базы данных.
     */
    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    /**
     * Получает имя пользователя базы данных из конфигураций.
     *
     * @return Имя пользователя базы данных.
     */
    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    /**
     * Получает пароль базы данных из конфигураций.
     *
     * @return Пароль базы данных.
     */
    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }


}
