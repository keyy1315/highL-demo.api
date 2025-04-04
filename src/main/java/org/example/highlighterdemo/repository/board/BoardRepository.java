package org.example.highlighterdemo.repository.board;

import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.entity.enums.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, String>, BoardRepositoryCustom {
    List<Board> findAllByCategory(Category category, Sort by);

    List<Board> findAllByMember(Member member);
}
