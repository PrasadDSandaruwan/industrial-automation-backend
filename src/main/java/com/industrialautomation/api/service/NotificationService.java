package com.industrialautomation.api.service;


import com.industrialautomation.api.dao.NotificationRepository;
import com.industrialautomation.api.dto.admin.NotificationDTO;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Object addNotification(String notification_name, String slug, String sound_track) {

        Notification notification = notificationRepository.getNotificationBySlug(slug);

        if(notification!= null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Slug is not unique");

        notification = new Notification();
        notification.setNotification_name(notification_name);
        notification.setSlug(slug);
        notification.setSound_track(sound_track);

        notificationRepository.save(notification);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Added.");

    }

    public Object editNotification(String notification_name, String slug, String sound_track, Long id) {
        Notification notification = notificationRepository.getNotificationBySlug(slug);

        if(notification!= null){
            if(notification.getId()!=id)
                return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Slug is not unique");
        }




        notification = notificationRepository.getNotificationById(id);
        if(notification==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such Notification.");
        notification.setNotification_name(notification_name);
        notification.setSlug(slug);
        notification.setSound_track(sound_track);

        notificationRepository.save(notification);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Edited.");

    }

    public Object allNotifications() {
        List<Notification> notifications = notificationRepository.getAllNotifications();

        List<NotificationDTO> result = new LinkedList<>();

        for(Notification n: notifications)
            result.add(new NotificationDTO(n));

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data Fetched Successfully.",result);
    }
}
