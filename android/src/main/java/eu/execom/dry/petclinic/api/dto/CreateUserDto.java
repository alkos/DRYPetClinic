package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class CreateUserDto {
    private Integer role;
    private String username;
    private String password;

    public CreateUserDto(Integer role, String username, String password) {
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public CreateUserDto() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
