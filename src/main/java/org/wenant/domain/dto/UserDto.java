package org.wenant.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "role"})
public class UserDto {

    private final Role role;
    private final String username;


    public UserDto(
            @JsonProperty("username") String username,
            @JsonProperty("role") Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }


    public enum Role {
        ADMIN,
        USER
    }
}
