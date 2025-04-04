package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.entity.Notification;
import org.example.highlighterdemo.model.entity.enums.NotificationAction;
import org.example.highlighterdemo.model.requestDTO.NotificationRequest;
import org.example.highlighterdemo.repository.notification.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;

    public Notification setNotification(String userName, NotificationRequest req, String receiver) {
        Member member = memberService.getUsersByUserId(userName);

        Notification notification = Notification.create(member, req, receiver);
        return notificationRepository.save(notification);
    }

    public Member getReceiver(NotificationRequest req) {
        switch (req.referenceType()) {
            case "board" -> {
                return boardService.getBoardById(req.referenceId()).getMember();
            }
            case "member" -> {
                return memberService.getUsersByUserId(req.referenceId());
            }
            case "comment" -> {
                return commentService.getCommentById(req.referenceId()).getMember();
            }
            case null, default ->
                    throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find reference type");
        }
    }

    public List<Notification> getNotifications(String receiver) {
        if (receiver == null || memberService.existById(receiver)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find user");
        }
        return notificationRepository.findAllByReceiverOrderByCreatedDate(receiver);
    }

    public String setDescription(String receiver, NotificationRequest req) {
        switch (req.action()) {
            case NotificationAction.COMMENT -> {
                String boardTitle = boardService.getBoardById(req.referenceId()).getTitle();
                return receiver + " comment on \"" + boardTitle + "\"";
            }
            case NotificationAction.FOLLOW -> {
                return null;
            }
            case NotificationAction.LIKE -> {
                if (req.referenceType().equals("board")) {
                    String boardTitle = boardService.getBoardById(req.referenceId()).getTitle();
                    return receiver + " like on \"" + boardTitle + "\"";
                } else if (req.referenceType().equals("comment")) {
                    String commentContent = commentService.getCommentById(req.referenceId()).getContent();
                    String cut = commentContent.length() > 30 ? commentContent.substring(0, 30) + "..." : commentContent;
                    return receiver + " like on \"" + cut + "\"";
                } else {
                    return null;
                }
            }
            case NotificationAction.MENTION -> {
                String boardTitle = boardService.getBoardById(req.referenceId()).getTitle();
                return receiver + " mention on \"" + boardTitle + "\"";
            }
            case null, default ->
                    throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "cannot find reference type");
        }
    }

//    public boolean readNotification(List<String> id) {
//        for (String i : id) {
//
//        }
//    }
}
