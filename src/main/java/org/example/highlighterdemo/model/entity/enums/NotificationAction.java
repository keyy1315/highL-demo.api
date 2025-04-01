package org.example.highlighterdemo.model.entity.enums;

import lombok.Getter;

@Getter
public enum NotificationAction {
    LIKE("like"), COMMENT("comment"), FOLLOW("follow");
    private final String value;

    private NotificationAction(String value) {
        this.value = value;
    }
}
