package org.example.highlighterdemo.model.requestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
///     클라이언트에서 보낼 데이터
///     서버에서 member 의 객체를 생성하거나 수정 할 때 사용
@Schema(description = "회원 요청")
public record MemberRequest(
        @Schema(description = "회원 ID")
        @NotBlank(message = "user_id : 필수")
        String userId,
        @Schema(description = "비밀번호")
        @NotBlank(message = "password: 필수")
        String password,
        @Schema(description = "회원 닉네임")
        String userName,
        @Schema(description = "라이엇 id 태그")
        String nameTag,
        @Schema(description = "티어")
        String tier,
        @Schema(description = "권한")
        String role
) {
}
