package org.example.highlighterdemo.model.responseDTO;

import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.Tag;

import java.util.List;

public record BoardResponse(
        String id,
        String title,
        String content,
        String videoUrl,
        MemberResponse member,
        List<Tag> tags,
        int likes,
        int comments
) {
    public static BoardResponse create(Board board, int comments) {
        return new BoardResponse(board.getId(), board.getTitle(), board.getContent(), board.getVideo(),
                MemberResponse.create(board.getMember()), board.getTags(), board.getLikes(), comments);
    }
}
