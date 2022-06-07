package com.industrialautomation.api.dto.admin;

import com.industrialautomation.api.model.Machine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NearestMachineDTO {

    private Long id;
    private String name;
    private String slug;


    public NearestMachineDTO(Machine machine) {
        this.id = machine.getId();
        this.name = machine.getName();
        this.slug = machine.getSlug();
    }
}
