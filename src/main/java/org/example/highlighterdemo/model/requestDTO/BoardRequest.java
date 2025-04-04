package org.example.highlighterdemo.model.requestDTO;

import java.util.List;

public record BoardRequest(
        String title,
        String content,
        List<String> tags,
        String category
) {}
