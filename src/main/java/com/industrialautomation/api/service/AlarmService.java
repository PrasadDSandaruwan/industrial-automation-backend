package com.industrialautomation.api.service;


import com.industrialautomation.api.dao.AlarmRepository;
import com.industrialautomation.api.dao.MachineRepository;
import com.industrialautomation.api.dto.admin.AlarmDetailsDTO;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.Alarm;
import com.industrialautomation.api.model.Machine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class AlarmService {
    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    public Object addAlarm(String alarm_name, String slug, Long machine_id) {

        try {
            Alarm alarm = alarmRepository.getAlarmBySlug(slug);
            if(alarm!=null)
                return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Slug is not unique");
        }catch (Exception e){
            e.printStackTrace();
        }

        Machine machine;
        try {
            machine = machineRepository.getMachineById(machine_id);
            if(machine==null)
                return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Machine doesn't exists");
        }catch (Exception e){
            e.printStackTrace();
        }

        Alarm alarm = new Alarm();
        alarm.setAlarm_name(alarm_name);
        alarm.setAdded_at(LocalDateTime.now());
        alarm.setMachine(new Machine(machine_id));
        alarm.setSlug(slug);

        alarmRepository.save(alarm);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Added.");

    }

    public Object edit(String alarm_name, String slug, Long machine_id, Long id) {
        try {
            Alarm alarm = alarmRepository.getAlarmBySlug(slug);
            if(alarm!=null){
                if (alarm.getId()!=id)
                    return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Slug is not unique");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        Machine machine;
        try {
            machine = machineRepository.getMachineById(machine_id);
            if(machine==null)
                return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Machine doesn't exists");
        }catch (Exception e){
            e.printStackTrace();
        }

        Alarm alarm=null;
        try {
            alarm = alarmRepository.getAlarmById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(alarm==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such Alarm exists.");

        alarm.setSlug(slug);
        alarm.setAlarm_name(alarm_name);
        alarm.setMachine(new Machine(machine_id));

        alarmRepository.save(alarm);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Edited.");


    }

    public Object deleteAlarm(Long id) {
        Alarm alarm = alarmRepository.getAlarmById(id);
        if (alarm ==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Invalid Alarm Id");
        alarm.setDeleted(LocalDateTime.now());
        alarmRepository.save(alarm);
        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Deleted.");
    }

    public Object allAlarms() {

        List<Alarm> alarms;
        alarms = alarmRepository.getAlarmsByDeletedIsNullWithMachine();

        List<AlarmDetailsDTO> alarmDetailsDTOS = new LinkedList<>();
        for (Alarm a : alarms)
            alarmDetailsDTOS.add(new AlarmDetailsDTO(a));

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data fetched successfully.",alarmDetailsDTOS);


    }

    public Object alarmDetails(Long id) {
        Alarm alarm = alarmRepository.getAlarmsByIdWithMachine(id);
        if (alarm ==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Invalid Alarm Id.");
        return  new DefaultResponseDTO(200,ResponseStatus.OK,"Data fetched successfully.", new AlarmDetailsDTO(alarm));
    }
}
