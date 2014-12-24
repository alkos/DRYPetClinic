package eu.execom.dry.petclinic.api.dto;

import eu.execom.dry.petclinic.api.enumeration.*;

public class ReadUserDto {
    private Integer id;

    public ReadUserDto(Integer id) {
        this.id = id;
    }

    public ReadUserDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
