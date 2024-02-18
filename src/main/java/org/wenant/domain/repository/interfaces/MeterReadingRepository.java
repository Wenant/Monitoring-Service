package org.wenant.domain.repository.interfaces;

import org.wenant.domain.model.MeterReading;
import org.wenant.domain.model.MeterTypeCatalog;
import org.wenant.domain.model.User;

import java.time.YearMonth;
import java.util.List;

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
     * @return Список показаний счетчиков.
     */
    List<MeterReading> getAllForUser(User user);

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
     * @return Список показаний счетчиков.
     */
    List<MeterReading> getLatestMeterReadings(User user);

    /**
     * Проверяет, существуют ли показания счетчика для указанного пользователя, даты и типа счетчика.
     *
     * @param user             Пользователь, для которого нужно проверить наличие показаний.
     * @param date             Дата, для которой нужно проверить наличие показаний.
     * @param meterTypeCatalog Тип счетчика, для которого нужно проверить наличие показаний.
     * @return true, если показания существуют, иначе false.
     */
    boolean isMeterReadingExists(User user, YearMonth date, MeterTypeCatalog meterTypeCatalog);
}
