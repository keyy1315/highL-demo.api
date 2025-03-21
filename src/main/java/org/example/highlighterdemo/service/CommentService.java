package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.entity.Comment;
import org.example.highlighterdemo.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public int getCommentCnt(String boardId) {
        return commentRepository.findAllByBoardId(boardId).size();
    }
}
