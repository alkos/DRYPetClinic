package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class AdminUsersDto {
    private Integer role;
    private Integer from;
    private Integer maxRowCount;

    public AdminUsersDto(Integer role, Integer from, Integer maxRowCount) {
        this.role = role;
        this.from = from;
        this.maxRowCount = maxRowCount;
    }

    public AdminUsersDto() {
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getMaxRowCount() {
        return maxRowCount;
    }

    public void setMaxRowCount(Integer maxRowCount) {
        this.maxRowCount = maxRowCount;
    }
}
