package org.wenant;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.wenant.database.DatabaseConnector;
import org.wenant.starter.domain.repository.JdbcAuditRepository;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class MonitoringServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitoringServiceApplication.class, args);
    }
    @Bean
    public JdbcAuditRepository auditRepository() throws SQLException {
        Connection connection = DatabaseConnector.connection();
        return new JdbcAuditRepository(connection);
    }
}