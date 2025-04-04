package org.example.highlighterdemo.repository.comment;

import org.example.highlighterdemo.model.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {
    List<Comment> findAllByBoardId(String boardId);
    List<Comment> findAllByBoardId(String boardId, Sort sort);
}
