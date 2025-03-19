package org.example.highlighterdemo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.highlighterdemo.model.entity.enums.MemberRole;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
///     Member entity...
///     티어와 1:n 연관관계이다.
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "member")
@Schema(description = "member entity")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "member pk")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "회원 아이디")
    private String userId;

    @Column
    @Schema(description = "라이엇 id 또는 회원 이름")
    private String userName;

    @Column
    @Schema(description = "라이엇 id 태그")
    private String nameTag;

    @Column(nullable = false)
    @Schema(description = "비밀번호")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "권한")
    private MemberRole role;

    @Column
    @Schema(description = "현재 티어")
    private String tier;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Schema(description = "계정 활성화 여부")
    private boolean isActive;

    @Column(length = 1000)
    @Schema(description = "인증 토큰")
    private String refreshToken;

    public static Member create(MemberRequest req) {
        return Member.builder()
                .id(null)
                .userId(req.userId())
                .userName(req.userName())
                .nameTag(req.nameTag())
                .password(pwEncoder(req.password()))
                .role(setMemberRole(req.role()))
                .tier(req.tier())
                .isActive(true)
                .build();
    }
    private static String pwEncoder(@NotBlank(message = "password: 필수") String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    private static MemberRole setMemberRole(String role) {
        if("ADMIN".equalsIgnoreCase(role)) {
            return MemberRole.ADMIN;
        } else {
            return MemberRole.USER;
        }
    }

    public Member addGameId(String tier, String userName, String nameTag) {
        this.tier = tier;
        this.userName = userName;
        this.nameTag = nameTag;
        return this;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
