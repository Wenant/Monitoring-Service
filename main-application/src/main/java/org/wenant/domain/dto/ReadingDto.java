package org.wenant.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReadingDto {
    private final Long typeId;
    private final double value;

    @JsonCreator
    public ReadingDto(@JsonProperty("typeId") Long typeId, @JsonProperty("value") double value) {
        this.typeId = typeId;
        this.value = value;
    }

    public Long getTypeId() {
        return typeId;
    }

    public double getValue() {
        return value;
    }
}