package org.wenant.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude
public class MeterTypeCatalogDto {

    private final Long id;
    private final String meterType;


    @JsonCreator
    public MeterTypeCatalogDto(
            @JsonProperty("id") Long id,
            @JsonProperty("meterType") String meterType) {
        this.id = id;
        this.meterType = meterType;
    }


    public Long getId() {
        return id;
    }

    public String getMeterType() {
        return meterType;
    }

}