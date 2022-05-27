package com.industrialautomation.api.dto.admin;

import com.industrialautomation.api.model.Machine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MachineDetailsDTO {

    private Long id;


    private String name;


    private String slug;

    private String license_number;


    private LocalDateTime added_at;

    private Integer is_automated;

    private LocalDateTime deleted;
    private ProductionLineDTO production_line;
    private MachineTypeDTO machine_type;

    public MachineDetailsDTO(Machine m) {
        this.id = m.getId();
        this.name = m.getName();
        this.slug = m.getSlug();
        this.license_number = m.getLicense_number();
        this.added_at = m.getAdded_at();
        this.is_automated = m.getIs_automated();
        this.deleted = m.getDeleted();
        this.production_line = new ProductionLineDTO( m.getProductionLine());
        this.machine_type = new MachineTypeDTO( m.getMachineType());
    }
}
