package org.example.highlighterdemo.model.responseDTO;

import lombok.Builder;
import org.example.highlighterdemo.model.entity.Notification;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        String id,
        String action,
        LocalDateTime createdDate,
        MemberResponse sender,
        String url
) {
    public static NotificationResponse create(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .action(String.valueOf(notification.getAction()))
                .createdDate(notification.getCreatedDate())
                .url(notification.getUrl())
                .sender(MemberResponse.create(notification.getSender()))
                .build();
    }
}
