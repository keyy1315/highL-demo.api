package org.example.highlighterdemo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.highlighterdemo.model.requestDTO.GameInfoRequest;

@Builder
@Getter
@Entity
@Table(name = "game_info")
@Schema(description = "member's game info")
@AllArgsConstructor
@NoArgsConstructor
public class GameInfo {
    @Id
    @Schema(description = "game_info pk(riot puuid)")
    private String id;

    @Column(nullable = false, name = "summoner_id")
    @Schema(description = "riot encrypted summonerId")
    private String summonerId;

    @Column
    @Schema(description = "user's tier")
    private String tier;

    @Column(name = "game_name")
    @Schema(description = "in game name")
    private String gameName;

    @Column(name = "tag_line")
    @Schema(description = "in game tagLine")
    private String tagLine;

    @Column
    @Schema(description = "game icon")
    private String iconUrl;

    @Column(nullable = false)
    @Schema(description = "active")
    private boolean isActive;

    private static String ICON_BASE_URL = "https://ddragon.leagueoflegends.com/cdn/15.6.1/img/profileicon/";

    public static GameInfo create(GameInfoRequest request) {
        return GameInfo.builder()
                .id(request.puuid())
                .summonerId(request.summonerId())
                .tier(request.tier())
                .gameName(request.gameName())
                .tagLine(request.tagLine())
                .iconUrl(ICON_BASE_URL + request.profileIconId() + ".png")
                .isActive(request.isActive())
                .build();
    }
}
