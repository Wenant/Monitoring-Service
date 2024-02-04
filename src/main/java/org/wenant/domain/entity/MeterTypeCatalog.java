package org.wenant.domain.entity;

/**
 * Класс, представляющий справочник типов счетчиков.
 */
public class MeterTypeCatalog {

    /**
     * Уникальный идентификатор типа счетчика.
     */
    private final Long id;

    /**
     * Тип счетчика.
     */
    private final String meterType;

    /**
     * Конструктор класса MeterTypeCatalog.
     *
     * @param id        Уникальный идентификатор типа счетчика.
     * @param meterType Тип счетчика.
     */
    public MeterTypeCatalog(Long id, String meterType) {
        this.id = id;
        this.meterType = meterType;
    }

    /**
     * Получить уникальный идентификатор типа счетчика.
     *
     * @return Уникальный идентификатор типа счетчика.
     */
    public Long getId() {
        return id;
    }

    /**
     * Получить тип счетчика.
     *
     * @return Тип счетчика.
     */
    public String getMeterType() {
        return meterType;
    }

    /**
     * Переопределение метода toString для удобного вывода информации о типе счетчика.
     *
     * @return Строковое представление объекта MeterTypeCatalog.
     */
    @Override
    public String toString() {
        return meterType + ": ";
    }
}
