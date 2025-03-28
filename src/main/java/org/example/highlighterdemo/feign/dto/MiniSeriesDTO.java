package org.example.highlighterdemo.feign.dto;

public record MiniSeriesDTO(
        int losses,
        String progress,
        int target,
        int wins) {}
