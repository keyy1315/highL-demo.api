package org.example.highlighterdemo.model.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.example.highlighterdemo.model.entity.Member;

///     서버에서 클라이언트에게 전달 할 데이터
///     사용자 생성 후 사용자의 정보를 전달하거나
///     클라이언트가 사용자의 정보를 get 할 때 보내 줄 용도 - 모든 컬럼을 전달 하는 것 보다는 필요한 정보만 전달하기 위함
@Schema(description = "회원 response")
public record MemberResponse(
        @Schema(description = "member pk") Long id,
        @Schema(description = "회원 아이디") String userId,
        @Schema(description = "회원 이름 (라이엇 아이디)") String userName,
        @Schema(description = "라이엇 아이디 태그") String nameTag,
        @Schema(description = "티어") String tier,
        @Schema(description = "계정 활성 여부") boolean isActive
) {
    public static MemberResponse create(Member member) {
        return new MemberResponse(
                member.getId(), member.getUserId(), member.getUserName(), member.getNameTag(), member.getTier(), member.isActive());
    }
}
