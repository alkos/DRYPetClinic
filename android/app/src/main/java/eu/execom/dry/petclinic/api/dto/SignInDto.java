package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class SignInDto {
    private String username;
    private String passwordHash;

    public SignInDto(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public SignInDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
