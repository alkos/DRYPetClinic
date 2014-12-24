package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class ReadUserResponseDto {
    private Integer id;
    private Integer role;
    private String username;

    public ReadUserResponseDto(Integer id, Integer role, String username) {
        this.id = id;
        this.role = role;
        this.username = username;
    }

    public ReadUserResponseDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
