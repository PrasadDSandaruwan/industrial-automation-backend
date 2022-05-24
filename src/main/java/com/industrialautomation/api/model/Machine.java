package com.industrialautomation.api.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "machine")
@Getter
@Setter
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
    private boolean is_automated;

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

}
