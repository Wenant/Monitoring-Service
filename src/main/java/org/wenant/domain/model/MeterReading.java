package org.wenant.domain.model;

import java.time.YearMonth;

/**
 * Класс, представляющий показания счетчика.
 */
public class MeterReading {

    /**
     * Пользователь, для которого введены показания.
     */
    private final User user;

    /**
     * Значение показаний счетчика.
     */
    private final double value;

    /**
     * Дата, к которой относятся показания.
     */
    private final YearMonth date;

    /**
     * Тип счетчика.
     */
    private final MeterTypeCatalog meterTypeCatalog;

    /**
     * Конструктор класса MeterReading.
     *
     * @param user             Пользователь, для которого введены показания.
     * @param value            Значение показаний счетчика.
     * @param date             Дата, к которой относятся показания.
     * @param meterTypeCatalog Тип счетчика.
     */
    public MeterReading(User user, double value, YearMonth date, MeterTypeCatalog meterTypeCatalog) {
        this.user = user;
        this.value = value;
        this.date = date;
        this.meterTypeCatalog = meterTypeCatalog;
    }

    /**
     * Получить тип счетчика.
     *
     * @return Тип счетчика.
     */
    public MeterTypeCatalog getMeterTypeCatalog() {
        return meterTypeCatalog;
    }

    /**
     * Получить пользователя, для которого введены показания.
     *
     * @return Пользователь.
     */
    public User getUser() {
        return user;
    }

    /**
     * Получить значение показаний счетчика.
     *
     * @return Значение показаний.
     */
    public double getValue() {
        return value;
    }

    /**
     * Получить дату, к которой относятся показания.
     *
     * @return Дата показаний.
     */
    public YearMonth getDate() {
        return date;
    }

    /**
     * Переопределение метода toString для удобного вывода информации о показаниях счетчика.
     *
     * @return Строковое представление объекта MeterReading.
     */
    @Override
    public String toString() {
        return "MeterReading{" +
                "user=" + user +
                ", value=" + value +
                ", date=" + date +
                ", meterType='" + meterTypeCatalog.getMeterType() + '\'' +
                '}';
    }
}
