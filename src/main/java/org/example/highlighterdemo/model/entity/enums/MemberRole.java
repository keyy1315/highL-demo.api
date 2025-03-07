package org.example.highlighterdemo.model.entity.enums;

import lombok.Getter;
///     사용자의 권한을 정의한 열거형 객체
///     admin, user 이외의 값을 입력하지 않기 위해 정의
@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }
}
