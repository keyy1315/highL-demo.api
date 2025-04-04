package org.example.highlighterdemo.model.entity.enums;

import lombok.Getter;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;

@Getter
public enum Category {
    MASTERY("mastery"), ISSUES("issues"), JUDGEMENT("judgement");

    private final String val;

    Category(String val) {
        this.val = val;
    }

    public static Category getCategory(String val) {
        switch (val) {
            case "mastery" -> {
                return Category.MASTERY;
            }
            case "issues" -> {
                return Category.ISSUES;
            }
            case "judgement" -> {
                return Category.JUDGEMENT;
            }
            case null, default ->
                    throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "wrong value category : " + val);
        }
    }
}
