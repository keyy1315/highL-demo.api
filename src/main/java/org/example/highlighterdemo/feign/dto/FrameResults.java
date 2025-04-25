package org.example.highlighterdemo.feign.dto;

public record FrameResults(
        boolean is_lol_frame,
        Minimap minimap,
        SkillBar skillBar
) {
}
