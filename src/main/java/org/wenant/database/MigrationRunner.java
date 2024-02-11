package org.wenant.database;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Класс для запуска миграций базы данных.
 */
public class MigrationRunner {

    /**
     * Запускает миграции базы данных.
     */
    public static void runMigrations() {
        try {
            Connection connection = DatabaseConnector.connection();
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции выполнены успешно");
        } catch (SQLException | LiquibaseException e) {
            System.out.println("SQLException в миграциях " + e.getMessage());
        }
    }
}
