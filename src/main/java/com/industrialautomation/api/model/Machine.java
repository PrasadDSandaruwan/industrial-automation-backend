package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "machine")
@Getter
@Setter
@NoArgsConstructor
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String slug;

    @Column(nullable = false)
    private String license_number;

    @Column(nullable = false)
    private LocalDateTime added_at;

    @Column(nullable = false)
    private Integer is_automated;

    private LocalDateTime deleted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "production_line_id", nullable = false)
    private ProductionLine productionLine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_type_id", nullable = false)
    private MachineType machineType;

    @OneToMany(mappedBy = "machine", fetch = FetchType.LAZY)
    private List<Command> commands;

    @OneToMany(mappedBy = "machine", fetch = FetchType.LAZY)
    private List<Alarm> alarms;

    public Machine(Long id) {
        this.id = id;
    }

    public Machine(Long id, String name, String slug, String license_number, LocalDateTime added_at, Integer is_automated, LocalDateTime deleted, ProductionLine productionLine, MachineType machineType) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.license_number = license_number;
        this.added_at = added_at;
        this.is_automated = is_automated;
        this.deleted = deleted;
        this.productionLine = productionLine;
        this.machineType = machineType;
    }
}
