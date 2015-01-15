package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class AuthenticationResponseDto {
    private String email;
    private Integer roleId;
    private String accessToken;
    private String refreshToken;

    public AuthenticationResponseDto(String email, Integer roleId, String accessToken, String refreshToken) {
        this.email = email;
        this.roleId = roleId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public AuthenticationResponseDto() {
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
