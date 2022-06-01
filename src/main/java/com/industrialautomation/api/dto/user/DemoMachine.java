package com.industrialautomation.api.dto.user;


import com.industrialautomation.api.model.Machine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class DemoMachine {

    private Long id;
    private String name;
    private Float rate;
    private Float temp;
    private Float init_rate;
    private Float init_temp;

    public DemoMachine(Machine machine) {
        this.id = machine.getId();
        this.name = machine.getName();
        this.rate = machine.getCurrent_rate();
        this.temp = machine.getCurrent_temp();
        this.init_rate = machine.getInt_rate();
        this.init_temp = machine.getInt_tempe();
    }
}
