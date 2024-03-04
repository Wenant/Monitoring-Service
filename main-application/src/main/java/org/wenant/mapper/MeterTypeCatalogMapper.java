package org.wenant.mapper;


import org.mapstruct.Mapper;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.model.MeterTypeCatalog;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeterTypeCatalogMapper {

    MeterTypeCatalogDto meterTypeToMeterTypeCatalogDto(MeterTypeCatalog meterTypeCatalog);


    List<MeterTypeCatalogDto> meterTypeListToDtoList(List<MeterTypeCatalog> meterTypeCatalogList);


}
