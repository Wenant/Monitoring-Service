package org.wenant.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude
public class AuthenticationDto {
    private final String username;
    private final String password;



    @JsonCreator
    public AuthenticationDto(@JsonProperty("username") String username,
                             @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
