package org.example.highlighterdemo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.example.highlighterdemo.model.entity.enums.MemberRole;

import java.util.List;
///     Member entity...
///     티어와 1:n 연관관계이다.
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Schema(description = "시즌 별 티어")
    private List<Tiers> tiers;
}
