package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class AuthenticationResponseDto {
    private String username;
    private Integer roleId;
    private String accessToken;
    private String refreshToken;

    public AuthenticationResponseDto(String username, Integer roleId, String accessToken, String refreshToken) {
        this.username = username;
        this.roleId = roleId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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
