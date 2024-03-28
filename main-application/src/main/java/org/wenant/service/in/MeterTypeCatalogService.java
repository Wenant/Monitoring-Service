package org.wenant.service.in;

import org.springframework.stereotype.Service;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.model.MeterTypeCatalog;
import org.wenant.domain.repository.MeterTypeCatalogRepository;
import org.wenant.mapper.MeterTypeCatalogMapper;

import java.util.List;

/**
 * Сервис для работы с каталогом типов счетчиков.
 * Предоставляет функциональность добавления новых типов счетчиков и получения списка всех доступных типов.
 */
@Service
public class MeterTypeCatalogService {

    private final MeterTypeCatalogRepository meterTypeCatalogRepository;
    private final MeterTypeCatalogMapper meterTypeCatalogMapper;


    /**
     * Конструктор класса MeterTypeCatalogService.
     *
     * @param meterTypeCatalogRepository Репозиторий каталога типов счетчиков.
     */
    public MeterTypeCatalogService(MeterTypeCatalogRepository meterTypeCatalogRepository,
                                   MeterTypeCatalogMapper meterTypeCatalogMapper) {

        this.meterTypeCatalogRepository = meterTypeCatalogRepository;
        this.meterTypeCatalogMapper = meterTypeCatalogMapper;
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
    public List<MeterTypeCatalogDto> getMeterTypes() {
        List<MeterTypeCatalogDto> newCatalog =
                meterTypeCatalogMapper.meterTypeListToDtoList(meterTypeCatalogRepository.getAllMeterTypes());
        return newCatalog;
    }

    public MeterTypeCatalog getMeterTypeById(Long id) {
        return meterTypeCatalogRepository.getMeterTypeById(id);
    }
}
