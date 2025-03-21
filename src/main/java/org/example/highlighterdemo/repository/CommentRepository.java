package org.example.highlighterdemo.repository;

import org.example.highlighterdemo.model.entity.Comment;
import org.example.highlighterdemo.repository.custom.interfaces.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {
    List<Comment> findAllByBoardId(String boardId);
}
