package org.wenant.domain.repository.impl;

import org.springframework.stereotype.Repository;
import org.wenant.database.DatabaseConnector;
import org.wenant.domain.model.MeterTypeCatalog;
import org.wenant.domain.repository.interfaces.MeterTypeCatalogRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для типов счетчиков через JDBC.
 */
@Repository
public class JdbcMeterTypeCatalogRepository implements MeterTypeCatalogRepository {

    @Override
    public void addMeterTypeCatalog(String meterType) {
        String sql = "INSERT INTO ylab_hw.meter_type_catalog(type) VALUES(?)";
        try (Connection connection = DatabaseConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, meterType);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding meter type catalog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public MeterTypeCatalog getMeterTypeById(Long id) {
        String sql = "SELECT * FROM ylab_hw.meter_type_catalog WHERE id = ?";

        try (Connection connection = DatabaseConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String type = resultSet.getString("type");
                    return new MeterTypeCatalog(id, type);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while getting meter type by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MeterTypeCatalog> getAllMeterTypes() {
        List<MeterTypeCatalog> catalog = new ArrayList<>();
        String sql = "SELECT * FROM ylab_hw.meter_type_catalog";

        try (Connection connection = DatabaseConnector.connection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String meterType = resultSet.getString("type");

                MeterTypeCatalog meterTypeCatalog = new MeterTypeCatalog(id, meterType);
                catalog.add(meterTypeCatalog);
            }

        } catch (SQLException e) {
            System.out.println("Error while getting all meter types: " + e.getMessage());
            e.printStackTrace();
        }

        return catalog;
    }
}
