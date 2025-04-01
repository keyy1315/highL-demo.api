package org.example.highlighterdemo.model.requestDTO;

import java.time.LocalDateTime;

public record NotificationRequest(
        String id,
        String memberId,
        String action,
        LocalDateTime time,
        String url
) {}
