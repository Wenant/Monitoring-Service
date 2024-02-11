package org.wenant.service.in;

import org.wenant.domain.entity.MeterReading;
import org.wenant.domain.entity.MeterTypeCatalog;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.MeterReadingRepository;

import java.time.YearMonth;
import java.util.List;

/**
 * Сервис для управления показаниями счетчиков.
 * Обеспечивает добавление новых показаний, получение и анализ показаний для пользователя.
 */
public class MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;

    /**
     * Конструктор класса MeterReadingService.
     *
     * @param meterReadingRepository Репозиторий для сохранения и получения данных о показаниях счетчиков.
     */
    public MeterReadingService(MeterReadingRepository meterReadingRepository) {
        this.meterReadingRepository = meterReadingRepository;
    }

    /**
     * Добавляет новые показания счетчиков.
     *
     * @param meterReading Объект MeterReading, представляющий новые показания.
     */
    public void addNew(MeterReading meterReading) {
        meterReadingRepository.save(meterReading);
    }

    /**
     * Получает все показания счетчиков для конкретного пользователя.
     *
     * @param user Пользователь, для которого нужно получить показания счетчиков.
     * @return Список MeterReading, содержащий показания счетчиков для указанного пользователя.
     */
    public List<MeterReading> getAllForUser(User user) {
        return meterReadingRepository.getAllForUser(user);
    }

    /**
     * Получает показания счетчиков для конкретного пользователя на определенную дату.
     *
     * @param user Пользователь, для которого нужно получить показания счетчиков.
     * @param date Дата, на которую нужно получить показания счетчиков.
     * @return Список MeterReading, представляющий показания счетчиков на указанную дату.
     */
    public List<MeterReading> getByUserAndDate(User user, YearMonth date) {
        return meterReadingRepository.getByUserAndDate(user, date);
    }

    /**
     * Получает актуальные показания счетчиков для конкретного пользователя.
     *
     * @param user Пользователь, для которого нужно получить актуальные показания счетчиков.
     * @return Список MeterReading, содержащий актуальные показания счетчиков для указанного пользователя.
     */
    public List<MeterReading> getLatestMeterReadings(User user) {
        return meterReadingRepository.getLatestMeterReadings(user);
    }

    /**
     * Проверяет существование показаний счетчика для конкретного пользователя, на указанную дату и тип счетчика.
     *
     * @param user             Пользователь, для которого выполняется проверка.
     * @param date             Дата, для которой выполняется проверка.
     * @param meterTypeCatalog Тип счетчика, для которого выполняется проверка.
     * @return true, если показания существуют, в противном случае - false.
     */
    public boolean isMeterReadingExists(User user, YearMonth date, MeterTypeCatalog meterTypeCatalog) {
        return meterReadingRepository.isMeterReadingExists(user, date, meterTypeCatalog);
    }
}
