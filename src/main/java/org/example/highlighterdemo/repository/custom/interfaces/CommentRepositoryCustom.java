package org.example.highlighterdemo.repository.custom.interfaces;

import org.example.highlighterdemo.model.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findAllByBoardIdSorted(String boardId);
}
