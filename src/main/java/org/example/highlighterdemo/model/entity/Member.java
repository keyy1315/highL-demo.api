package org.example.highlighterdemo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.highlighterdemo.model.entity.enums.MemberRole;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "member")
@Schema(description = "member entity")
public class Member {
    @Id
    @Schema(description = "member pk : user set id")
    private String id;

    @Column(nullable = false)
    @Schema(description = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "auth")
    private MemberRole role;

    @Column(nullable = false, columnDefinition = "boolean default true", name = "is_active")
    @Schema(description = "active")
    private boolean isActive;

    @Column(length = 1000)
    @Schema(description = "refresh token")
    private String refreshToken;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_id", referencedColumnName = "id")
    @Schema(description = "following members")
    private List<Member> member;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_info_id", referencedColumnName = "id")
    @Schema(description = "connect riot id")
    private GameInfo gameInfo;

    public static Member create(MemberRequest req) {
        return Member.builder()
                .id(req.id())
                .password(pwEncoder(req.password()))
                .role(setMemberRole(""))
                .isActive(true)
                .gameInfo(null)
                .build();
    }

    private static String pwEncoder(@NotBlank(message = "password: 필수") String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    private static MemberRole setMemberRole(String role) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            return MemberRole.ADMIN;
        } else {
            return MemberRole.USER;
        }
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateGameInfo(GameInfo info) {
        this.gameInfo = info;
    }
}
