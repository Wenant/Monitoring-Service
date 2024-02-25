package org.wenant.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.model.MeterTypeCatalog;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeterTypeCatalogMapper {

    MeterTypeCatalogMapper INSTANCE = Mappers.getMapper(MeterTypeCatalogMapper.class);


    MeterTypeCatalogDto meterTypeToMeterTypeCatalogDto(MeterTypeCatalog meterTypeCatalog);


    List<MeterTypeCatalogDto> meterTypeListToDtoList(List<MeterTypeCatalog> meterTypeCatalogList);


}
