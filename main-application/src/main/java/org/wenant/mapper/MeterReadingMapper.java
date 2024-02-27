package org.wenant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.model.MeterReading;

import java.time.YearMonth;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MeterReadingMapper {

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
