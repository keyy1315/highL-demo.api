package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.entity.enums.NotificationAction;
import org.example.highlighterdemo.model.requestDTO.NotificationRequest;
import org.example.highlighterdemo.model.responseDTO.NotificationResponse;
import org.example.highlighterdemo.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Tag(name = "WebsocketController", description = "websocket")
public class WebsocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    /**
     * 1. 로그인 이후 /notification/{로그인 아이디} 에 구독
     * 2. 로그인 한 사용자 댓글/좋아요/팔로우 하면 실시간으로 /app/comment.add ... 로 데이터 받음
     * 3. receiver 가 로그인 되어 있으면 실시간으로 알림 감
     */
    @Operation(description = "댓글 작성 알림")
    @MessageMapping("/comment.add")
    public void addComment(@AuthenticationPrincipal UserDetails user, NotificationRequest req) {
        Member receiver = notificationService.getReceiver(req);
        NotificationResponse notiResponse = NotificationResponse.create(
                notificationService.setNotification(user.getUsername(), NotificationAction.COMMENT, req, receiver.getId()));

        messagingTemplate.convertAndSend("/notifications/" + receiver.getId(), notiResponse);
    }

}
