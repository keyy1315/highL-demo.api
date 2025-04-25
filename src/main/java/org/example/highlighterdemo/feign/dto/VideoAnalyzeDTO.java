package org.example.highlighterdemo.feign.dto;

public record VideoAnalyzeDTO(
        boolean is_lol_video,
        double confidence,
        FrameResults frame_results
){}
