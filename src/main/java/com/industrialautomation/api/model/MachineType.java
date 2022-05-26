package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "machine_type")
@Getter
@Setter
@NoArgsConstructor
public class MachineType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String machine_type_name;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "machineType", fetch = FetchType.LAZY)
    private List<Machine> machines;

    public MachineType(Long id) {
        this.id = id;
    }

    public MachineType(Long id, String machine_type_name, String slug) {
        this.id = id;
        this.machine_type_name = machine_type_name;
        this.slug = slug;
    }
}
