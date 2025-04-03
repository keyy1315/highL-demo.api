package org.example.highlighterdemo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

///     토큰 관련 비즈니스 로직
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERID_CLAIM = "username";
    private static final String BEARER = "Bearer ";
    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.expiration}")
    private Long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    ///  access 토큰 생성 메소드 claim 에 userId 를 입력하여 서버에서 토큰을 가져왔을 때
    ///  어떤 사용자인지 알 수 있게 함
    public String createAccessToken(String userId) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpiration * 1000))
                .withClaim(USERID_CLAIM, userId)
                .sign(Algorithm.HMAC256(secret));
    }

    ///  refresh 토큰 생성 메소드
    ///  access 토큰 만료 시 재발급 해주는 용도이다.
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration * 1000))
                .sign(Algorithm.HMAC256(secret));
    }

    ///     토큰 삭제 메소드
    ///     refresh 토큰이 만료되어 강제 로그아웃 할 때 / 사용자 로그아웃 할 때 사용
    @Transactional
    public void deleteTokens(String userId, HttpServletResponse response) {
        memberRepository.findById(userId).ifPresentOrElse(
                member -> {
                    member.updateRefreshToken(null);
                    memberRepository.save(member);
                },
        () -> {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Not found user");
        });
        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_SUBJECT, null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_SUBJECT, null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    ///     생성된 토큰을 클라이언트의 쿠키에 전달하는 메소드
    ///     HttpOnly 옵션과 SameSite 속성을 설정하여 보안성을 높임
    public void sendTokens(HttpServletRequest request,HttpServletResponse response, String accessToken, String refreshToken) {
        boolean isLocal = request.getServerName().equals("localhost");

        Cookie accessCookie = new Cookie(ACCESS_TOKEN_SUBJECT, accessToken);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setMaxAge(Math.toIntExact(accessExpiration));
        accessCookie.setAttribute("SameSite", "Strict");
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie(REFRESH_TOKEN_SUBJECT, refreshToken);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setMaxAge(Math.toIntExact(refreshExpiration));
        refreshCookie.setAttribute("SameSite", "Strict");
        response.addCookie(refreshCookie);
    }

    ///  클라이언트의 request 에서 access 토큰 값만 추출하는 메소드
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return Optional.empty();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ACCESS_TOKEN_SUBJECT)) {
                return cookie.getValue().describeConstable();
            }
        }
        return Optional.empty();
    }

    ///  클라이언트의 request 에서 refresh 토큰 값만 추출하는 메소드
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return Optional.empty();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_SUBJECT)) {
                return cookie.getValue().describeConstable();
            }
        }
        return Optional.empty();
    }

    ///  access 토큰 값을 통해 userId 의 값을 추출
    public Optional<String> extractUserId(String token) {
        try {
            return Optional.ofNullable(
                    JWT.require(Algorithm.HMAC256(secret))
                            .build()
                            .verify(token)
                            .getClaim(USERID_CLAIM)
                            .asString());
        } catch (Exception e) {
            log.error("cannot extract userId : {}", e.getMessage());
            return Optional.empty();
        }
    }

    ///  토큰이 만료되었는지 / 맞는 토큰인지 검증하는 메소드
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token : {}", e.getMessage());
            return false;
        }
    }
}
