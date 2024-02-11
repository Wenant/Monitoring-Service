package org.wenant.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "meterType", "value", "date"})
public class MeterReadingDto {


    private final String username;

    private final double value;

    private final String date;

    private final String meterType;


    @JsonCreator
    public MeterReadingDto(
            @JsonProperty("username") String username,
            @JsonProperty("meterTypeCatalog") String meterType,
            @JsonProperty("value") double value,
            @JsonProperty("date") String date) {
        this.username = username;
        this.meterType = meterType;
        this.value = value;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getMeterType() {
        return meterType;
    }

    public double getValue() {
        return value;
    }


    public String getDate() {
        return date;
    }


}