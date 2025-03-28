package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.requestDTO.CommentRequest;
import org.example.highlighterdemo.model.responseDTO.CommentResponse;
import org.example.highlighterdemo.model.responseDTO.NotificationResponse;
import org.example.highlighterdemo.service.CommentService;
import org.example.highlighterdemo.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class WebsocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final CommentService commentService;
    private final NotificationService notificationService;

    @Operation(description = "댓글 작성")
    @MessageMapping("/comment.add")
    public void addComment(CommentRequest commentRequest,
                           @AuthenticationPrincipal UserDetails user) {
        CommentResponse commentResponse= CommentResponse.create(commentService.setComments(user.getUsername(), commentRequest));
        NotificationResponse notiResponse = NotificationResponse.create(notificationService.setNotification(user.getUsername()));

        messagingTemplate.convertAndSend("/comments/" + commentRequest.boardId(), commentResponse);
        messagingTemplate.convertAndSend("/notifications/" + commentRequest.boardId());
    }
}
