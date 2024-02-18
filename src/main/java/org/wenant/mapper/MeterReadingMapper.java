package org.wenant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.entity.MeterReading;

import java.time.YearMonth;
import java.util.List;

@Mapper
public interface MeterReadingMapper {
    MeterReadingMapper INSTANCE = Mappers.getMapper(MeterReadingMapper.class);


    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "meterTypeCatalog.meterType", target = "meterType")
    @Mapping(source = "date", target = "date", qualifiedByName = "mapYearMonthToString")
    MeterReadingDto meterReadingToMeterReadingDto(MeterReading meterReading);

    List<MeterReadingDto> meterReadingListToDtoList(List<MeterReading> meterReading);

    @Named("mapYearMonthToString")
    default String mapYearMonthToString(YearMonth yearMonth) {
        return yearMonth.toString();
    }
}
