package org.wenant.domain.repository.impl;

import org.springframework.stereotype.Repository;
import org.wenant.database.DatabaseConnector;
import org.wenant.domain.model.User;
import org.wenant.domain.model.User.Role;
import org.wenant.domain.repository.UserRepository;
import org.wenant.starter.annotations.EnableAudit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория пользователей через JDBC.
 */
@Repository
@EnableAudit
public class UserRepositoryImpl implements UserRepository {

    private String getRoleForDb(User user) {
        return user.getRole().name();
    }

    private Role setRoleForDb(String role) {
        return Role.valueOf(role);
    }


    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO ylab_hw.users (username, password, role) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, getRoleForDb(user));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> listOfUsers = new ArrayList<>();
        String sql = "SELECT * FROM ylab_hw.users";

        try (Connection connection = DatabaseConnector.connection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                User user = new User(id, username, password, setRoleForDb(role));
                listOfUsers.add(user);
            }

            return listOfUsers;
        } catch (SQLException e) {
            System.out.println("Error while getting all users: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM ylab_hw.users WHERE username = ?";

        try (Connection connection = DatabaseConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");

                    return new User(id, username, password, setRoleForDb(role));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while getting user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM ylab_hw.users WHERE id = ?";

        try (Connection connection = DatabaseConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");

                    return new User(id, username, password, setRoleForDb(role));
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while getting user by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
