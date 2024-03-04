package org.wenant.domain.dto;

public class ResponseDto {

    private final String response;
    private final String message;

    public ResponseDto(String response, String message) {
        this.response = response;
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

}