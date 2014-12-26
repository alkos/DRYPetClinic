package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class AdminUsersResponseDto {
    private Integer id;
    private String username;
    private Integer roleId;

    public AdminUsersResponseDto(Integer id, String username, Integer roleId) {
        this.id = id;
        this.username = username;
        this.roleId = roleId;
    }

    public AdminUsersResponseDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
