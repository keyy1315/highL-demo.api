package org.example.highlighterdemo.feign.dto;

import java.util.Optional;

public record LeagueEntryDTO(
        String leagueId,
        String summonerId,
        String puuid,
        String queueType,
        String tier,
        String rank,
        int leaguePoints,
        int wins,
        int losses,
        boolean hotStreak,
        boolean veteran,
        boolean freshBlood,
        boolean inactive,
        Optional<MiniSeriesDTO> miniSeries
) {}
