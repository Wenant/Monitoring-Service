package org.wenant.service.in;

import org.wenant.domain.entity.MeterReading;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.MeterReadingRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

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
     * @return Map, содержащая показания счетчиков, сгруппированные по месяцам.
     */
    public Map<YearMonth, List<MeterReading>> getAllForUser(User user) {
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
     * @return Map, содержащая актуальные показания счетчиков, сгруппированные по месяцам.
     */
    public Map<YearMonth, List<MeterReading>> getLatestMeterReadings(User user) {
        return meterReadingRepository.getLatestMeterReadings(user);
    }

    /**
     * Проверяет, существуют ли показания счетчика для указанного пользователя, даты и типа счетчика.
     *
     * @param user      Пользователь, для которого нужно проверить наличие показаний счетчика.
     * @param date      Дата, на которую нужно проверить наличие показаний счетчика.
     * @param meterType Тип счетчика, для которого нужно проверить наличие показаний.
     * @return true, если показания счетчика существуют, в противном случае false.
     */
    public boolean isMeterReadingExists(User user, YearMonth date, String meterType) {
        return meterReadingRepository.isMeterReadingExists(user, date, meterType);
    }
}
