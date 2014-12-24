package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class AuthenticationResponseDto {
    private String username;
    private Integer roleId;
    private String authenticationCode;

    public AuthenticationResponseDto(String username, Integer roleId, String authenticationCode) {
        this.username = username;
        this.roleId = roleId;
        this.authenticationCode = authenticationCode;
    }

    public AuthenticationResponseDto() {
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

    public String getAuthenticationCode() {
        return authenticationCode;
    }

    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }
}
