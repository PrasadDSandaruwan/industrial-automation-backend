package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification,Long> {
}
