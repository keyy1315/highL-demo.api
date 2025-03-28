package org.example.highlighterdemo.repository.board;

import org.example.highlighterdemo.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, String>, BoardRepositoryCustom {
}
