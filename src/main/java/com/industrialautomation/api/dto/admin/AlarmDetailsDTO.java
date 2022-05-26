package com.industrialautomation.api.dto.admin;

import com.industrialautomation.api.model.Alarm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AlarmDetailsDTO {

    private Long id;
    private String alarm_name;
    private String slug;
    private LocalDateTime added_at;
    private LocalDateTime deleted;
    private NearestMachineDTO nearest_machine;

    public AlarmDetailsDTO(Alarm alarm){
        this.id = alarm.getId();
        this.alarm_name = alarm.getAlarm_name();
        this.slug = alarm.getSlug();
        this.added_at = alarm.getAdded_at();
        this.deleted = alarm.getDeleted();
        this.nearest_machine = new NearestMachineDTO(alarm.getMachine());
    }
}
