package org.example.highlighterdemo.model.requestDTO;

public record GameInfoRequest(
        String userId,
        String gameName,
        String tagLine
) {}
