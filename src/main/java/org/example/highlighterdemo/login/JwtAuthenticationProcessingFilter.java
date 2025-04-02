package org.example.highlighterdemo.login;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.repository.member.MemberRepository;
import org.example.highlighterdemo.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

///     jwt 토큰의 인증 단계
///     클라이언트의 쿠키에 access/refresh token 달려있는지 검증하고 있을 경우 사용자의 정보를 security context 에 저장하여
///     사용자의 정보를 가져올 수 있다.
///
///     토큰이 없을 경우는 인증을 하지 않기 때문에 securityConfig 파일에서 정의한 url 을 제외하고 접근할 수 없다.
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        String refreshToken = jwtService.extractRefreshToken(request).orElse(null);

        if (accessToken != null && refreshToken != null) {
            if (jwtService.isTokenValid(accessToken)) {
                jwtService.extractUserId(accessToken).flatMap(
                        memberRepository::findById
                ).ifPresent(this::saveAuthentication);
            } else {
                checkRefreshTokenAndResetTokens(request, response, refreshToken);
            }
        } else if (accessToken == null && refreshToken != null) {
            checkRefreshTokenAndResetTokens(request, response, refreshToken);
        }
        filterChain.doFilter(request, response);
    }

    private void checkRefreshTokenAndResetTokens(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        Optional<Member> member = memberRepository.findByRefreshToken(refreshToken);
        if (jwtService.isTokenValid(refreshToken)) {
            member.ifPresentOrElse(m -> {
                        /// Refresh Token Rotate
                        String newRefreshToken = jwtService.createRefreshToken();
                        m.updateRefreshToken(newRefreshToken);

                        jwtService.sendTokens(response, jwtService.createAccessToken(m.getId()), newRefreshToken);
                    },
                    () -> {
                        throw new CustomException(ErrorCode.FORBIDDEN, "cannot find user with refresh token");
                    });
        } else {
            member.ifPresentOrElse(m ->
                            jwtService.deleteTokens(m.getId(), response),
                    () -> {
                        throw new CustomException(ErrorCode.FORBIDDEN, "cannot find user with refresh token");
                    });
            throw new CustomException(ErrorCode.FORBIDDEN, "Invalid refresh token");
        }
    }

    private void saveAuthentication(Member member) {
        UserDetails user =
                User.builder()
                        .username(member.getId())
                        .password(member.getPassword())
                        .roles(member.getRole().name())
                        .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, authoritiesMapper.mapAuthorities(user.getAuthorities()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
