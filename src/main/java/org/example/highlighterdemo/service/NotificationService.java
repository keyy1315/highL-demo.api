package org.example.highlighterdemo.service;

import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.controller.NotificationController;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
public class NotificationService {
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(0L);

        try {
            emitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        NotificationController.sseEmitterMap.put(userId, emitter);

        emitter.onCompletion(() -> NotificationController.sseEmitterMap.remove(userId));
        emitter.onTimeout(() -> NotificationController.sseEmitterMap.remove(userId));
        emitter.onError((ex) -> NotificationController.sseEmitterMap.remove(userId));

        return emitter;
    }
}
