package com.industrialautomation.api.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ChangeRatioAlarmDTO {

    private Long id;
    private String alarm_name;
    private String massage;

    private String type;

    public ChangeRatioAlarmDTO(Long machine_id, String machine_name, String massage,String type) {
        this.id = machine_id;
        this.alarm_name = machine_name;
        this.massage = massage;
        this.type = type;
    }
}
