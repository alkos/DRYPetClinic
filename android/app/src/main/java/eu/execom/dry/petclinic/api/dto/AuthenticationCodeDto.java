package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class AuthenticationCodeDto {
    private String authenticationCode;

    public AuthenticationCodeDto(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

    public AuthenticationCodeDto() {
    }

    public String getAuthenticationCode() {
        return authenticationCode;
    }

    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }
}
