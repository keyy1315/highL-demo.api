package org.example.highlighterdemo.feign.dto;

public record Minimap(
        boolean is_minimap,
        int circle_count,
        double color_score,
        double structure_score,
        double texture_complexity,
        boolean has_corners
) {
}
