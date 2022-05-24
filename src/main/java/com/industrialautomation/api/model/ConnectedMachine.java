package com.industrialautomation.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "connected_machine")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectedMachine {

    @EmbeddedId
    ConnectedMachineKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("machine_id")
    @JoinColumn(name = "machine_id")
    Machine machine;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("connected_machine_id")
    @JoinColumn(name = "connected_machine_id")
    Machine connectedMachine;

    @Column(nullable = false)
    private Float rate;


}
