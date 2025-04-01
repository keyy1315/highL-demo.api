package org.example.highlighterdemo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Action action;
    @Column
    @Schema(description = "created time")
    private LocalDateTime createdDate;
    @Column
    @Schema(description = "url for route")
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Member member;

    enum Action {
        LIKE, COMMENT, FOLLOW;
    }
}
