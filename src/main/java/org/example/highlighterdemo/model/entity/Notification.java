package org.example.highlighterdemo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.highlighterdemo.model.entity.enums.NotificationAction;
import org.example.highlighterdemo.model.requestDTO.NotificationRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "notification")
@Schema(description = "tag entity")
public class Notification {
    @Id
    @Schema(description = "notification pk")
    private String id;
    @Column
    @Schema(description = "action")
    private NotificationAction action;
    @Column
    @Schema(description = "created time")
    private LocalDateTime createdDate;
    @Column
    @Schema(description = "url for route")
    private String url;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Member sender;
    @Column(nullable = false)
    private String receiver;
    @Column(nullable = false)
    private boolean isRead;

    public static Notification create(Member member, NotificationRequest req, String receiver) {
        String url = "/";
        if ("comment".equals(req.referenceType())) url += "board/" + req.referenceId();
        else url += req.referenceType() + "/" + req.referenceId();

        return Notification.builder()
                .id(UUID.randomUUID().toString())
                .action(req.action())
                .createdDate(LocalDateTime.now())
                .url(url)
                .sender(member)
                .receiver(receiver)
                .isRead(false)
                .build();
    }

    public void readNotification() {
        this.isRead = true;
    }
}
