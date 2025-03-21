package org.example.highlighterdemo.repository.custom.interfaces;

import org.example.highlighterdemo.model.entity.Board;

import java.util.List;

public interface CustomRepository {
    List<Board> orderByCommentCnt(boolean desc);
}
