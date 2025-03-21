package org.example.highlighterdemo.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.QBoard;
import org.example.highlighterdemo.model.entity.QComment;
import org.example.highlighterdemo.repository.custom.interfaces.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CostomRepositoryImpl implements CustomRepository {
    private final JPAQueryFactory queryFactory;
    @Autowired
    public CostomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<Board> orderByCommentCnt(boolean desc) {
        QComment comment = QComment.comment;
        QBoard board = QBoard.board;

        return queryFactory
                .selectFrom(board)
                .leftJoin(comment).on(comment.board.id.eq(board.id))
                .groupBy(board.id)
                .orderBy(desc ? comment.count().desc() : comment.count().asc())
                .fetch();
    }
}
