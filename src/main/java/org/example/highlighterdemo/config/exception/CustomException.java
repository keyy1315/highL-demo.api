package org.example.highlighterdemo.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
///     에러처리 - RuntimeException 을 상속받았음
@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
