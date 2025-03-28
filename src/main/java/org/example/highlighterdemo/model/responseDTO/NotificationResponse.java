package org.example.highlighterdemo.model.responseDTO;

public record NotificationResponse(
        String id,
        String action,
        String createdDate,
        MemberResponse sender,
        String url
) {}
