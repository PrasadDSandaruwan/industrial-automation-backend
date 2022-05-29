package com.industrialautomation.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.service.MachineService;
import com.industrialautomation.api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/v1/notification/add")
    public Object addNotification(@RequestBody JsonNode jsonNode){

        try {
            String notification_name = (jsonNode.hasNonNull("notification_name")) ? jsonNode.get("notification_name").asText(): null;
            String slug = (jsonNode.hasNonNull("slug")) ? jsonNode.get("slug").asText(): null;
            String sound_track = (jsonNode.hasNonNull("sound_track")) ? jsonNode.get("sound_track").asText(): null;
            return  notificationService.addNotification(notification_name,slug,sound_track);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @PostMapping("/v1/notification/edit/{id}")
    public Object editNotification(@RequestBody JsonNode jsonNode, @PathVariable Long id){

        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Notification Id missing");
        try {
            String notification_name = (jsonNode.hasNonNull("notification_name")) ? jsonNode.get("notification_name").asText(): null;
            String slug = (jsonNode.hasNonNull("slug")) ? jsonNode.get("slug").asText(): null;
            String sound_track = (jsonNode.hasNonNull("sound_track")) ? jsonNode.get("sound_track").asText(): null;
            return  notificationService.editNotification(notification_name,slug,sound_track,id);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @GetMapping("/v1/notification/all")
    public Object allNotification(){
        try {
            return notificationService.allNotifications();
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }


}
