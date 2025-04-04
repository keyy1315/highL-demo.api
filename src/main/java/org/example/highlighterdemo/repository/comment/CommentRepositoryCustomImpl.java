package org.example.highlighterdemo.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.entity.Comment;
import org.example.highlighterdemo.model.entity.QComment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findAllByBoardIdSorted(String boardId) {
        QComment comment = QComment.comment;

        return queryFactory.selectFrom(comment)
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.createdDate.desc())
                .fetch();
    }
}
