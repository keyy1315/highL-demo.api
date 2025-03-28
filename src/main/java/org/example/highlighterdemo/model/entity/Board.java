package org.example.highlighterdemo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
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
    @Schema(description = "video url")
    private String video;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
                .title(req.title())
                .content(req.content())
                .member(member)
                .video(fileUrl)
                .tags(tags)
                .build();
    }
}
