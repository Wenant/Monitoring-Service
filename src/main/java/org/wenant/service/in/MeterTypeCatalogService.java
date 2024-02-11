package org.wenant.service.in;

import org.wenant.domain.entity.MeterTypeCatalog;
import org.wenant.domain.repository.MeterTypeCatalogRepository;

import java.util.List;

/**
 * Сервис для работы с каталогом типов счетчиков.
 * Предоставляет функциональность добавления новых типов счетчиков и получения списка всех доступных типов.
 */
public class MeterTypeCatalogService {
    private final MeterTypeCatalogRepository meterTypeCatalogRepository;

    /**
     * Конструктор класса MeterTypeCatalogService.
     *
     * @param meterTypeCatalogRepository Репозиторий каталога типов счетчиков.
     */
    public MeterTypeCatalogService(MeterTypeCatalogRepository meterTypeCatalogRepository) {
        this.meterTypeCatalogRepository = meterTypeCatalogRepository;
    }

    /**
     * Добавляет новый тип счетчика в каталог.
     *
     * @param meterType Новый тип счетчика для добавления.
     */
    public void addMeterType(String meterType) {
        meterTypeCatalogRepository.addMeterTypeCatalog(meterType);
    }

    /**
     * Получает список всех доступных типов счетчиков.
     *
     * @return Список типов счетчиков.
     */
    public List<MeterTypeCatalog> getMeterTypes() {
        return meterTypeCatalogRepository.getAllMeterTypes();
    }
}
