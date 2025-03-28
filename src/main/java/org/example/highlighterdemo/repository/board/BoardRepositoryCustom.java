package org.example.highlighterdemo.repository.board;

import org.example.highlighterdemo.model.entity.Board;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> orderByCommentCnt(boolean desc);
}
