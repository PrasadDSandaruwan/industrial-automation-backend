package com.industrialautomation.api.dto.user;

import com.industrialautomation.api.dto.admin.NearestMachineDTO;
import com.industrialautomation.api.model.Alarm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DemoAlarm {

    private Long id;
    private String alarm_name;
    private String slug;


    public DemoAlarm(Alarm alarm){
        this.id = alarm.getId();
        this.alarm_name = alarm.getAlarm_name();
        this.slug = alarm.getSlug();

    }
}
