package com.industrialautomation.api.dto.admin;


import com.industrialautomation.api.model.MachineType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MachineTypeDTO {

    private Long id;

    private String machine_type_name;


    private String slug;

    public MachineTypeDTO(MachineType m) {
        this.id = m.getId();
        this.machine_type_name = m.getMachine_type_name();
        this.slug = m.getSlug();
    }
}
