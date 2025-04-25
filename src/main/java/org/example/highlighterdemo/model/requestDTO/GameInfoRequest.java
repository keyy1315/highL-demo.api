package org.example.highlighterdemo.model.requestDTO;

public record GameInfoRequest(
       String puuid,
       String summonerId,
       String gameName,
       String tagLine,
       String profileIconId,
       String tier,
       boolean isActive
) {}
