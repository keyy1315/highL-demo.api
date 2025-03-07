package org.example.highlighterdemo.mapStruct;

import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.entity.enums.MemberRole;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.model.responseDTO.MemberResponse;
import org.mapstruct.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
///     객체 매핑해주는 라이브러리 사용
///     entity 에 setter 을 사용하기 때문에 추후 정적 팩토리 메소드를 사용하거나
///     builder 패턴으로 변경 할 예정
@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Named("pwEncoder")
    static String pwEncoder(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Named("toMemberRole")
    static MemberRole toMemberRole(String role) {
        if("ADMIN".equalsIgnoreCase(role)) {
            return MemberRole.ADMIN;
        } else {
            return MemberRole.USER;
        }
    }

    MemberResponse toResponse(Member member);

    @Mapping(source = "password", target = "password", qualifiedByName = "pwEncoder")
    @Mapping(source = "role", target = "role", qualifiedByName = "toMemberRole")
    Member toMember(MemberRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "refreshToken", source = "refreshToken", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Member updateRefreshToken(Member hasTokenMember, @MappingTarget Member member);
}
