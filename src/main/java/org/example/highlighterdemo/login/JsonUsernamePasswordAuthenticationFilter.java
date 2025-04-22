package org.example.highlighterdemo.login;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

///     기본 폼 기반 인증이 아닌 JSON 인증 요청을 처리하는 필터
///     POST 메소드로 요청이 들어올 시 콘텐츠 타입을 확인하고
///     userId, password 추출 후 UsernamePasswordAuthenticationToken 을 생성해준다.
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/login";
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USERID_KEY = "userId";
    private static final String PASSWORD_KEY = "password";

    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);
    private final ObjectMapper objectMapper;
    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(!request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException(
                    "Authentication Content-Type not supported: " + request.getContentType());
        }
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> map = objectMapper.readValue(messageBody, new TypeReference<>() {});
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(map.get(USERID_KEY), map.get(PASSWORD_KEY));

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
