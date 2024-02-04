package org.wenant.domain.repository;

import org.wenant.domain.entity.MeterTypeCatalog;

import java.util.List;

/**
 * Интерфейс для работы с каталогом типов счетчиков.
 */
public interface MeterTypeCatalogRepository {

    /**
     * Добавляет новый тип счетчика в каталог.
     *
     * @param meterType Тип счетчика для добавления.
     */
    void addMeterTypeCatalog(String meterType);

    /**
     * Получает тип счетчика по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор типа счетчика.
     * @return Объект типа счетчика, если найден, иначе null.
     */
    MeterTypeCatalog getMeterTypeById(Long id);

    /**
     * Получает список всех типов счетчиков.
     *
     * @return Список всех типов счетчиков.
     */
    List<MeterTypeCatalog> getAllMeterTypes();

}
