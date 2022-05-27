package com.industrialautomation.api.dto.admin;


import com.industrialautomation.api.model.ProductionLine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProductionLineDTO {

    private Long id;

    private String line_name;

    private String slug;

    public ProductionLineDTO(ProductionLine p) {
        this.id = p.getId();
        this.line_name = p.getLine_name();
        this.slug = p.getSlug();
    }
}
