package org.wenant.starter.domain.repository;


import org.wenant.starter.domain.entity.Audit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JdbcAuditRepository implements AuditRepository {

    private final Connection connection;

    public JdbcAuditRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Audit audit) {
        String sql = "INSERT INTO ylab_hw.audit_log (user_id, action, date, table_name, new_value) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, audit.getUserId());
            preparedStatement.setString(2, audit.getAction());
            preparedStatement.setTimestamp(3, audit.getDate());
            preparedStatement.setString(4, audit.getTableName());
            preparedStatement.setString(5, audit.getNewValue());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while adding audit log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Long getUserIdByUsername(String username) {
        String sql = "SELECT id FROM ylab_hw.users WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while getting user ID by username: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
