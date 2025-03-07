package org.example.highlighterdemo.filter.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
///  로그인 실패 했을 경우 실행되는 필터
///  메소드 오류 시 405 에러 반환, 이외의 오류는 400 에러 반환
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String err = exception.getMessage();
        if (err.contains("Method not supported")) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Authentication failed: " + err);
        }
    }
}
