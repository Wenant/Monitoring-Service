package org.wenant.domain.repository;

import org.wenant.domain.entity.MeterReading;
import org.wenant.domain.entity.User;

import java.time.YearMonth;
import java.util.*;

/**
 * Реализация MeterReadingRepository, хранящая показания счетчиков в памяти.
 */
public class InMemoryMeterReadingRepository implements MeterReadingRepository {

    private final Map<User, Map<YearMonth, List<MeterReading>>> meterReadingsMap = new HashMap<>();

    /**
     * Сохраняет показания счетчика в репозитории.
     *
     * @param meterReading Показания счетчика для сохранения.
     */
    @Override
    public void save(MeterReading meterReading) {
        meterReadingsMap.computeIfAbsent(meterReading.getUser(), k -> new LinkedHashMap<>())
                .computeIfAbsent(meterReading.getDate(), k -> new ArrayList<>())
                .add(meterReading);
    }

    /**
     * Получает все показания счетчиков для указанного пользователя.
     *
     * @param user Пользователь, для которого нужно получить показания счетчиков.
     * @return Map, где ключ - год и месяц, значение - список показаний счетчиков.
     */
    @Override
    public Map<YearMonth, List<MeterReading>> getAllForUser(User user) {
        return meterReadingsMap.getOrDefault(user, Collections.emptyMap());
    }

    /**
     * Получает показания счетчиков для указанного пользователя и месяца.
     *
     * @param user Пользователь, для которого нужно получить показания счетчиков.
     * @param date Месяц, для которого нужно получить показания счетчиков.
     * @return Список показаний счетчиков для указанного пользователя и месяца.
     */
    @Override
    public List<MeterReading> getByUserAndDate(User user, YearMonth date) {
        return meterReadingsMap.getOrDefault(user, Collections.emptyMap())
                .getOrDefault(date, Collections.emptyList());
    }

    /**
     * Получает последние показания счетчиков для указанного пользователя.
     *
     * @param user Пользователь, для которого нужно получить последние показания счетчиков.
     * @return Map, где ключ - год и месяц, значение - список последних показаний счетчиков.
     */
    @Override
    public Map<YearMonth, List<MeterReading>> getLatestMeterReadings(User user) {
        return meterReadingsMap.getOrDefault(user, Collections.emptyMap());
    }

    /**
     * Проверяет, существуют ли показания счетчика для указанного пользователя, месяца и типа счетчика.
     *
     * @param user      Пользователь, для которого нужно проверить существование показаний.
     * @param date      Месяц, для которого нужно проверить существование показаний.
     * @param meterType Тип счетчика, для которого нужно проверить существование показаний.
     * @return true, если существуют показания счетчика, в противном случае - false.
     */
    @Override
    public boolean isMeterReadingExists(User user, YearMonth date, String meterType) {
        return meterReadingsMap.getOrDefault(user, Collections.emptyMap())
                .getOrDefault(date, Collections.emptyList())
                .stream()
                .anyMatch(meterReading -> meterReading.getMeterType().equals(meterType));
    }
}
