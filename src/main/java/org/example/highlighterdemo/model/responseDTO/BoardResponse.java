package org.example.highlighterdemo.model.responseDTO;

import lombok.Builder;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BoardResponse(
        String id,
        String title,
        String content,
        String videoUrl,
        MemberResponse member,
        List<Tag> tags,
        int likes,
        int views,
        int comments,
        LocalDateTime createdDate
) {
    public static BoardResponse create(Board board, int comments) {
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .videoUrl(board.getVideo())
                .member(MemberResponse.create(board.getMember()))
                .tags(board.getTags())
                .likes(board.getLikes())
                .views(board.getViews())
                .comments(comments)
                .createdDate(board.getCreatedDate())
                .build();

    }
}
