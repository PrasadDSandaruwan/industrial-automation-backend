package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "production_line")
@Getter
@Setter
@NoArgsConstructor
public class ProductionLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String line_name;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "productionLine", fetch = FetchType.LAZY)
    private List<Machine> machines;

    public ProductionLine(Long id, String line_name, String slug) {
        this.id = id;
        this.line_name = line_name;
        this.slug = slug;
    }

    public ProductionLine(Long id) {
        this.id = id;
    }
}
