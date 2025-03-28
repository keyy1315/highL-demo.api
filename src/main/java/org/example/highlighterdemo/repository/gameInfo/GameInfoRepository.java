package org.example.highlighterdemo.repository.gameInfo;

import org.example.highlighterdemo.model.entity.GameInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameInfoRepository extends JpaRepository<GameInfo, String> {

}
