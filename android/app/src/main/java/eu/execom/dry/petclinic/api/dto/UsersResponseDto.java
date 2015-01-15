package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class UsersResponseDto {
    private Integer id;
    private String email;
    private Integer roleId;

    public UsersResponseDto(Integer id, String email, Integer roleId) {
        this.id = id;
        this.email = email;
        this.roleId = roleId;
    }

    public UsersResponseDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
