package org.example.highlighterdemo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "comment")
@Schema(description = "comment entity")
public class Comment {
    @Id
    @Schema(description = "comment pk")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Comment parentComment;

    @Column
    @Schema(description = "comment content")
    private String content;

    @Column
    @Schema(description = "like counts")
    private int likes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @Schema(description = "writer")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    @Schema(description = "board Id")
    private Board board;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "created date")
    private LocalDateTime createdDate;

    public static Comment create(Member member, Board board, String content, Comment parentComment) {
        return Comment.builder()
                .id(UUID.randomUUID().toString())
                .parentComment(parentComment)
                .content(content)
                .member(member)
                .board(board)
                .createdDate(LocalDateTime.now())
                .build();
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
