package org.example.highlighterdemo.repository.notification;

import org.example.highlighterdemo.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findAllByReceiverOrderByCreatedDate(String receiver);
}
