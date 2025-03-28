package org.example.highlighterdemo.model.requestDTO;

public record CommentRequest(
        String content,
        String boardId
) {}
