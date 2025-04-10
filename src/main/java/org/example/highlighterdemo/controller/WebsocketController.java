package org.example.highlighterdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.requestDTO.NotificationRequest;
import org.example.highlighterdemo.model.responseDTO.NotificationResponse;
import org.example.highlighterdemo.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebsocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    /**
     * 1. 로그인 이후 /notification/{로그인 아이디} 에 구독
     * 2. 로그인 한 사용자 댓글/좋아요/팔로우 하면 실시간으로 /app/comment.add ... 로 데이터 받음
     * 3. receiver 가 로그인 되어 있으면 실시간으로 알림 감
     */
    @MessageMapping("/noti.add")
    public void addComment(Principal principal, NotificationRequest req) {
        Member receiver = notificationService.getReceiver(req);

        if (receiver.getId().equals(principal.getName())) return;

        NotificationResponse notiResponse = NotificationResponse.create(
                notificationService.setNotification(principal.getName(), req, receiver.getId()));

        messagingTemplate.convertAndSend("/notifications/" + receiver.getId(), notiResponse);
    }

    @MessageMapping("/noti.read")
    public void read(Principal principal, List<String> ids) {
        log.info("Read notification with id {}", ids);
//        boolean result = notificationService.readNotification(ids);

//        messagingTemplate.convertAndSend("/notifications/" +principal.getName(), result);
    }
}
