package org.example.highlighterdemo.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.repository.member.MemberRepository;
import org.example.highlighterdemo.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.util.Objects;

///     로그인 성공시 실행되는 필터
///     access token, refresh token 생성 후 클라이언트의 쿠키에 저장 후 db의 refreshToken 컬럼에 저장한다.
@RequiredArgsConstructor
public class LoginSuccessJWTProviderHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String userId = extractUserId(authentication);
        String accessToken = jwtService.createAccessToken(userId);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendTokens(request, response, accessToken, refreshToken);

        Member member = memberRepository.findById(userId).orElse(null);
        Objects.requireNonNull(member).updateRefreshToken(refreshToken);
        memberRepository.save(member);
    }

    private String extractUserId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
