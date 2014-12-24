package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class UpdateUserBodyDTO {
    private Integer role;
    private String password;

    public UpdateUserBodyDTO(Integer role, String password) {
        this.role = role;
        this.password = password;
    }

    public UpdateUserBodyDTO() {
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
