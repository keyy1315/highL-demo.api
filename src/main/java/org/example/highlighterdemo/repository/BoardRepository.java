package org.example.highlighterdemo.repository;

import org.example.highlighterdemo.model.entity.Board;
import org.example.highlighterdemo.repository.custom.interfaces.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, String>, CustomRepository {
}
