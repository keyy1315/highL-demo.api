package org.example.highlighterdemo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.enums.Category;
import org.example.highlighterdemo.model.requestDTO.BoardRequest;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "board")
@Schema(description = "board entity")
public class Board {
    @Id
    @Schema(description = "board pk")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "auth")
    private Category category;

    @Column
    @Schema(description = "title")
    private String title;

    @Column
    @Schema(description = "content")
    private String content;

    @Column
    @Schema(description = "likes count")
    private int likes;

    @Column
    @Schema(description = "view count")
    private int views;

    @Column
    @Schema(description = "video url")
    private String video;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToMany
    @Schema(description = "태그")
    @JoinTable(name = "board_tag",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "created date")
    private LocalDateTime createdDate;

    public static Board create(BoardRequest req, List<Tag> tags, String fileUrl, Member member) {
        return Board.builder()
                .id(UUID.randomUUID().toString())
                .category(setCategory(req.category()))
                .title(req.title())
                .content(req.content())
                .member(member)
                .video(fileUrl)
                .tags(tags)
                .build();
    }

    private static Category setCategory(String category) {
        return switch (category.toUpperCase()) {
            case "MASTERY" -> Category.MASTERY;
            case "ISSUES" -> Category.ISSUES;
            case "JUDGEMENT" -> Category.JUDGEMENT;
            default -> throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "Invalid category: " + category);
        };
    }

    public void updateBoard(String file, BoardRequest boardRequest) {
        List<Tag> tags = boardRequest.tags().stream().map(Tag::create).toList();

        this.video = file;

        this.title = boardRequest.title();
        this.content = boardRequest.content();
        this.tags = tags;
        this.category = setCategory(boardRequest.category());
    }

    public void likeBoard() {
        this.likes++;
    }

    public void addView() {
        this.views++;
    }
}
