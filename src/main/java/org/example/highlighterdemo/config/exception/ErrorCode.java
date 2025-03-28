package org.example.highlighterdemo.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
///     예외 처리를 명확하게 하기 위해 정의한 열거형 데이터들
///     비즈니스 로직 (서비스) 단에서 예외를 처리 할 때 사용함
/// - 예외처리 : 객체의 값을 db 에 저장해야되는데 db 값이 없으면???
/// 프론트단에서는 오류가 왜 나는지 알 수 없음 runtime 오류가 날 경우에는 무조건 500 에러가 반환되기 때문
/// 이걸 CustomException 을 사용하여 에러를 내가 정의하고
/// global handler exception 처리를 해버리면 내가 정의한 오류가 프론트에 똑같이 나가서
/// 무엇 때문에 서버 오류가 나는지 확인할 수 있음
@Getter
public enum ErrorCode {
    // 400 ~
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED"),
    // 500 ~
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE");

    private final String message;
    private final HttpStatus status;

    ErrorCode(HttpStatus status, String message) {
        this.message = message;
        this.status = status;
    }
}
