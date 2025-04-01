package org.example.highlighterdemo.model.requestDTO;

public record CommentRequest(
        String parentId,
        String content,
        String boardId
) {}
