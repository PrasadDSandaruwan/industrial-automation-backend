package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "production_line")
@Getter
@Setter
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
}
