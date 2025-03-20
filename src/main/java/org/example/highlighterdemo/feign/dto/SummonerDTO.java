package org.example.highlighterdemo.feign.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record SummonerDTO(
        @Schema(description = "encrypted account id")
        String accountId,
        @Schema(description = "icon summoner")
        int profileIconId,
        @Schema(description = "Date summoner was last modified specified as epoch milliseconds")
        long revisionDate,
        @Schema(description = "Encrypted summoner ID")
        String id,
        @Schema(description = "Encrypted PUUID")
        String puuid,
        @Schema(description = "Summoner level associated")
        long summonerLevel) {}
