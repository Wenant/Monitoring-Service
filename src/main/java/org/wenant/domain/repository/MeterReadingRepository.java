package org.wenant.domain.repository;

import org.wenant.domain.entity.MeterReading;
import org.wenant.domain.entity.User;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * Интерфейс для работы с показаниями счетчиков.
 */
public interface MeterReadingRepository {

    /**
     * Сохраняет новое показание счетчика.
     *
     * @param meterReading Показание счетчика для сохранения.
     */
    void save(MeterReading meterReading);

    /**
     * Получает все показания счетчиков для указанного пользователя.
     *
     * @param user Пользователь, для которого нужно получить показания.
     * @return Map, где ключ - это месяц и год, а значение - список показаний счетчиков.
     */
    Map<YearMonth, List<MeterReading>> getAllForUser(User user);

    /**
     * Получает показания счетчиков для указанного пользователя и даты.
     *
     * @param user Пользователь, для которого нужно получить показания.
     * @param date Дата, для которой нужно получить показания.
     * @return Список показаний счетчиков.
     */
    List<MeterReading> getByUserAndDate(User user, YearMonth date);

    /**
     * Получает последние актуальные показания счетчиков для указанного пользователя.
     *
     * @param user Пользователь, для которого нужно получить показания.
     * @return Map, где ключ - это месяц и год, а значение - список показаний счетчиков.
     */
    Map<YearMonth, List<MeterReading>> getLatestMeterReadings(User user);

    /**
     * Проверяет существование показания счетчика для указанного пользователя, даты и типа счетчика.
     *
     * @param user     Пользователь.
     * @param date     Дата.
     * @param meterType Тип счетчика.
     * @return true, если показание существует, иначе false.
     */
    boolean isMeterReadingExists(User user, YearMonth date, String meterType);

}
