package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "machine")
@Getter
@Setter
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

}
