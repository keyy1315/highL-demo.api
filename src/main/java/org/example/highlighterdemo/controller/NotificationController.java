package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.responseDTO.NotificationResponse;
import org.example.highlighterdemo.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/noti")
@Tag(name = "NotificationController", description = " 알림 API")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(description = "알림 조회")
    @GetMapping
    public List<NotificationResponse> getNotification(@AuthenticationPrincipal UserDetails user) {
        return notificationService.getNotifications(user.getUsername()).stream()
                .map(NotificationResponse::create).toList();
    }
}
