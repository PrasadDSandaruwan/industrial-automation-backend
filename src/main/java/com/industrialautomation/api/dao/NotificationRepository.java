package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification,Long> {

    Notification getNotificationBySlug(String slug);

    Notification getNotificationById(Long id);

    @Query("SELECT u FROM Notification u order by u.id")
    List<Notification> getAllNotifications();
}
