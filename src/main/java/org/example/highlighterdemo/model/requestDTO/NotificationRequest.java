package org.example.highlighterdemo.model.requestDTO;

import org.example.highlighterdemo.model.responseDTO.MemberResponse;

import java.time.LocalDateTime;

public record NotificationRequest(
        String id,
        MemberResponse member,
        String action,
        LocalDateTime time,
        String url
) {}
