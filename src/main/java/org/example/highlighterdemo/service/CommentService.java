package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.entity.Comment;
import org.example.highlighterdemo.model.requestDTO.CommentRequest;
import org.example.highlighterdemo.model.responseDTO.CommentResponse;
import org.example.highlighterdemo.repository.comment.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public int getCommentCnt(String boardId) {
        return commentRepository.findAllByBoardId(boardId).size();
    }

    public List<CommentResponse> getComments(String boardId) {
        List<Comment> comments = commentRepository.findAllByBoardIdSorted(boardId);

        List<Comment> parentComments =
                comments.stream().filter(comment -> comment.getParentId() == null).toList();

        return parentComments.stream()
                .map(parent -> buildHierarchy(parent, comments))
                .toList();
    }

    private CommentResponse buildHierarchy(Comment parent, List<Comment> comments) {
        List<CommentResponse> childComments = comments.stream()
                .filter(comment -> Objects.equals(comment.getParentId(), parent.getId()))
                .map(child -> buildHierarchy(child, comments))
                .toList();
        return CommentResponse.from(parent, childComments);
    }

    public Comment setComments(String username, CommentRequest commentRequest) {
        return null;
    }
}
