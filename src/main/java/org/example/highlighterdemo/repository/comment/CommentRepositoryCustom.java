package org.example.highlighterdemo.repository.comment;

import org.example.highlighterdemo.model.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findAllByBoardIdSorted(String boardId);
}
