package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.responseDTO.NotificationResponse;
import org.example.highlighterdemo.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebsocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Operation(description = "댓글 작성")
    @MessageMapping("/comment.add")
    public void addComment(@AuthenticationPrincipal UserDetails user) {
        NotificationResponse notiResponse = NotificationResponse.create(notificationService.setNotification(user.getUsername()));

        messagingTemplate.convertAndSend("/notifications/" + notiResponse);
    }
}
