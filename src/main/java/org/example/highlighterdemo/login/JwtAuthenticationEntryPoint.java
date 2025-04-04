package org.example.highlighterdemo.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
///     Optional 객체를 사용했기 때문에 토큰이 만료되었거나 인증에 실패한 토큰 모두 403 에러로 반환됨 - Optional.empty 반환하기 때문
///     토큰이 만료되었을 경우 401 에러를 반환하기 위한 엔트리 포인트
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
