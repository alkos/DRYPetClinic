package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class AccessTokenDto {
    private String accessToken;

    public AccessTokenDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccessTokenDto() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
