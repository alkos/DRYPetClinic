package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class UpdateUserDto {
    private Integer id;
    private Integer role;
    private String password;

    public UpdateUserDto(Integer id, Integer role, String password) {
        this.id = id;
        this.role = role;
        this.password = password;
    }

    public UpdateUserDto() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
