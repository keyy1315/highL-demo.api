package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.Comment;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.entity.Notification;
import org.example.highlighterdemo.model.entity.enums.NotificationAction;
import org.example.highlighterdemo.model.requestDTO.NotificationRequest;
import org.example.highlighterdemo.repository.board.BoardRepository;
import org.example.highlighterdemo.repository.comment.CommentRepository;
import org.example.highlighterdemo.repository.member.MemberRepository;
import org.example.highlighterdemo.repository.notification.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Notification setNotification(String userName, NotificationAction comment, NotificationRequest req, String receiver) {
        Member member = memberRepository.findById(userName).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find user"));

        Notification notification = Notification.create(member, comment, req, receiver);
        return notificationRepository.save(notification);
    }

    public Member getReceiver(NotificationRequest req) {
        switch (req.referenceType()) {
            case "board" -> {
                return boardRepository.findById(req.referenceId())
                        .map(Board::getMember)
                        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find board"));
            }
            case "member" -> {
                return memberRepository.findById(req.referenceId()).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find member"));
            }
            case "comment" -> {
                return commentRepository.findById(req.referenceId())
                        .map(Comment::getMember)
                        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find comment"));
            }
            case null, default ->
                    throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find reference type");
        }
    }

    public List<Notification> getNotifications(String receiver) {
        if(receiver == null || memberRepository.existsById(receiver)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find user");
        }
        return notificationRepository.findAllByReceiverOrderByCreatedDate(receiver);
    }
}
