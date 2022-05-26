package com.industrialautomation.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AlarmController {

    @Autowired
    private AlarmService alarmService;


    @PostMapping("/v1/alarm/add")
    public Object addAlarm(@RequestBody JsonNode jsonNode){
        /**
         * alarm_name
         * slug
         * nearest_machine_id
         * */
        try {
            String alarm_name = (jsonNode.hasNonNull("alarm_name")) ? jsonNode.get("alarm_name").asText(): null;
            String slug = (jsonNode.hasNonNull("slug")) ? jsonNode.get("slug").asText(): null;
            Long machine_id = (jsonNode.hasNonNull("nearest_machine_id")) ? jsonNode.get("nearest_machine_id").asLong(): null;

            if(alarm_name==null || slug==null || machine_id==null)
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");

            return alarmService.addAlarm(alarm_name,slug, machine_id);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @PostMapping("/v1/alarm/edit/{id}")
    public Object editAlarm(@RequestBody JsonNode jsonNode, @PathVariable Long id){
        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Alarm Id missing");
        try {
            String alarm_name = (jsonNode.hasNonNull("alarm_name")) ? jsonNode.get("alarm_name").asText(): null;
            String slug = (jsonNode.hasNonNull("slug")) ? jsonNode.get("slug").asText(): null;
            Long machine_id = (jsonNode.hasNonNull("nearest_machine_id")) ? jsonNode.get("nearest_machine_id").asLong(): null;

            if(alarm_name==null || slug==null || machine_id==null)
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");
            return alarmService.edit(alarm_name,slug, machine_id, id);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @DeleteMapping("/v1/alarm/delete/{id}")
    public Object deleteAlarm(@PathVariable Long id){
        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Alarm Id missing");
        try {
            return alarmService.deleteAlarm(id);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @GetMapping("/v1/alarm/all")
    public Object allAlarms(){
        try {
            return alarmService.allAlarms();
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @GetMapping("/v1/alarms/details/{id}")
    public Object alarmDetails(@PathVariable Long id){
        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Alarm Id missing");
        try {
            return alarmService.alarmDetails(id);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }
}
