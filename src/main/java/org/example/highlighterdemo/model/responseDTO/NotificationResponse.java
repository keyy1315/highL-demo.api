package org.example.highlighterdemo.model.responseDTO;

import lombok.Builder;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Notification;
import org.example.highlighterdemo.model.entity.enums.NotificationAction;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        String id,
        String action,
        String description,
        LocalDateTime createdDate,
        MemberResponse sender,
        String url
) {
    public static NotificationResponse create(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .action(setAction(notification.getAction()))
                .description(notification.getDescription())
                .createdDate(notification.getCreatedDate())
                .url(notification.getUrl())
                .sender(MemberResponse.create(notification.getSender()))
                .build();
    }

    public static String setAction(NotificationAction action) {
        switch (action) {
            case NotificationAction.COMMENT -> {
                return "COMMENT on your post";
            }
            case NotificationAction.FOLLOW -> {
                return "FOLLOW you";
            }
            case NotificationAction.LIKE -> {
                return "LIKE your post";
            }
            case NotificationAction.MENTION -> {
                return "MENTION you";
            }
            case null, default -> throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "enum is wrong");
        }
    }
}
