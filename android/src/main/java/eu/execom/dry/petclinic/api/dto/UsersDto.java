package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class UsersDto {
    private Integer from;
    private Integer maxRowCount;

    public UsersDto(Integer from, Integer maxRowCount) {
        this.from = from;
        this.maxRowCount = maxRowCount;
    }

    public UsersDto() {
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
