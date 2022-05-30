package com.industrialautomation.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter @Setter  @NoArgsConstructor
public class ChangeRateDTO {

    private Long machine_id;
    private String machine_name;
    private String command;
    private String massage;
    private Float rate;
    private Float temp;

    private Float init_rate;
    private Float init_temp;

    public ChangeRateDTO(Long machine_id, String machine_name, String command, String massage, Float rate, Float temp, Float init_rate, Float init_temp) {
        this.machine_id = machine_id;
        this.machine_name = machine_name;
        this.command = command;
        this.massage = massage;
        this.rate = rate;
        this.temp = temp;
        this.init_rate = init_rate;
        this.init_temp = init_temp;
    }
}
