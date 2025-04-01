package org.example.highlighterdemo.repository.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.QBoard;
import org.example.highlighterdemo.model.entity.QComment;
import org.example.highlighterdemo.model.entity.enums.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @Autowired
    public BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<Board> orderByCommentCnt(String category, boolean desc) {
        QComment comment = QComment.comment;
        QBoard board = QBoard.board;

        return queryFactory
                .selectFrom(board)
                .leftJoin(comment).on(comment.board.id.eq(board.id))
                .groupBy(board.id)
                .where(board.category.eq(Category.valueOf(category)))
                .orderBy(desc ? comment.count().desc() : comment.count().asc())
                .fetch();
    }
}
