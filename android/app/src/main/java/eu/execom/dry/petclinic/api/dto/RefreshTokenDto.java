package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class RefreshTokenDto {
    private String refreshToken;

    public RefreshTokenDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshTokenDto() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
