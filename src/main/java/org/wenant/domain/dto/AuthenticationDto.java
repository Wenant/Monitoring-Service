package org.wenant.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude
public class AuthenticationDto {
    private final String username;
    private final String password;
    private final Role role;


    @JsonCreator
    public AuthenticationDto(@JsonProperty("username") String username,
                             @JsonProperty("password") String password,
                             @JsonProperty("role") Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public enum Role {
        ADMIN,
        USER
    }
}
