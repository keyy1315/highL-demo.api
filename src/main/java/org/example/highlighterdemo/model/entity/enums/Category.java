package org.example.highlighterdemo.model.entity.enums;

import lombok.Getter;

@Getter
public enum Category {
    MASTERY("mastery"), ISSUES("issues"), JUDGEMENT("judgement");

    private final String val;

    Category(String val) {
        this.val = val;
    }
}
