package com.industrialautomation.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectedMachineKey implements Serializable {

    @Column(name = "machine_id")
    long machine_id;

    @Column(name = "connected_machine_id")
    long connected_machine_id;


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        ConnectedMachineKey key = (ConnectedMachineKey) obj;
        return key.getMachine_id() == machine_id && key.getConnected_machine_id() == connected_machine_id;
    }
}
