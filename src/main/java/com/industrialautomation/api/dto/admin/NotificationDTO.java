package com.industrialautomation.api.dto.admin;


import com.industrialautomation.api.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String notification_name;
    private String slug;
    private String sound_track;

    public NotificationDTO(Notification n) {
        this.id = n.getId();
        this.notification_name = n.getNotification_name();
        this.slug = n.getSlug();
        this.sound_track = n.getSound_track();
    }
}
