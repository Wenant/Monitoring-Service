package org.wenant.domain.repository.impl;

import org.springframework.stereotype.Repository;
import org.wenant.database.DatabaseConnector;
import org.wenant.domain.model.MeterReading;
import org.wenant.domain.model.MeterTypeCatalog;
import org.wenant.domain.model.User;
import org.wenant.domain.repository.MeterReadingRepository;
import org.wenant.domain.repository.MeterTypeCatalogRepository;
import org.wenant.starter.annotations.EnableAudit;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для показаний счетчиков через JDBC.
 */
@Repository
@EnableAudit
public class MeterReadingRepositoryImpl implements MeterReadingRepository {
    private final MeterTypeCatalogRepository meterTypeCatalogRepository;

    public MeterReadingRepositoryImpl(MeterTypeCatalogRepository meterTypeCatalogRepository) {
        this.meterTypeCatalogRepository = meterTypeCatalogRepository;
    }

    private Date getSqlDate(YearMonth yearMonth) {
        LocalDate localDate = yearMonth.atDay(1);
        return Date.valueOf(localDate);
    }

    private YearMonth setDate(Date date) {
        LocalDate localDate = date.toLocalDate();
        return YearMonth.of(localDate.getYear(), localDate.getMonth());
    }

    @Override
    public void addMeterReading(MeterReading meterReading) {
        String sql = "INSERT INTO ylab_hw.meter_readings (user_id, value, date, meter_type_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, meterReading.getUser().getId());
            preparedStatement.setDouble(2, meterReading.getValue());
            preparedStatement.setDate(3, getSqlDate(meterReading.getDate()));
            preparedStatement.setLong(4, meterReading.getMeterTypeCatalog().getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while saving meter reading: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<MeterReading> getAllForUser(User user) {
        String sql = "SELECT * FROM ylab_hw.meter_readings WHERE user_id = ?";
        List<MeterReading> meterReadingList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    double value = resultSet.getDouble("value");
                    YearMonth date = setDate(resultSet.getDate("date"));
                    MeterTypeCatalog meterTypeCatalog = meterTypeCatalogRepository.
                            getMeterTypeById(resultSet.getLong("meter_type_id"));

                    MeterReading meterReading = new MeterReading(user, value, date, meterTypeCatalog);
                    meterReadingList.add(meterReading);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while getting all meter readings for user: " + e.getMessage());
            e.printStackTrace();
        }
        return meterReadingList;
    }

    @Override
    public List<MeterReading> getByUserAndDate(User user, YearMonth date) {
        String sql = "SELECT * FROM ylab_hw.meter_readings WHERE user_id = ? AND date = ?";
        List<MeterReading> meterReadingList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user.getId());
            preparedStatement.setDate(2, getSqlDate(date));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    double value = resultSet.getDouble("value");
                    MeterTypeCatalog meterTypeCatalog =
                            meterTypeCatalogRepository.getMeterTypeById(resultSet.getLong("meter_type_id"));

                    MeterReading meterReading = new MeterReading(user, value, date, meterTypeCatalog);
                    meterReadingList.add(meterReading);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while getting meter readings by user and date: " + e.getMessage());
            e.printStackTrace();
        }
        return meterReadingList;
    }

    @Override
    public List<MeterReading> getLatestMeterReadings(User user) {
        String sql = "SELECT * FROM ylab_hw.meter_readings WHERE user_id = ? AND date = (SELECT MAX(date) FROM ylab_hw.meter_readings WHERE user_id = ?)";
        List<MeterReading> meterReadingList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, user.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    YearMonth date = setDate(resultSet.getDate("date"));
                    double value = resultSet.getDouble("value");
                    MeterTypeCatalog meterTypeCatalog = meterTypeCatalogRepository.
                            getMeterTypeById(resultSet.getLong("meter_type_id"));

                    MeterReading meterReading = new MeterReading(user, value, date, meterTypeCatalog);
                    meterReadingList.add(meterReading);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while getting latest meter readings for user: " + e.getMessage());
            e.printStackTrace();
        }
        return meterReadingList;
    }

    @Override
    public boolean isMeterReadingExists(User user, YearMonth date, MeterTypeCatalog meterTypeCatalog) {
        String sql = "SELECT COUNT(*) FROM ylab_hw.meter_readings WHERE user_id = ? AND date = ? AND meter_type_id = ?";

        try (Connection connection = DatabaseConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user.getId());
            preparedStatement.setDate(2, getSqlDate(date));
            preparedStatement.setLong(3, meterTypeCatalog.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while checking if meter reading exists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
