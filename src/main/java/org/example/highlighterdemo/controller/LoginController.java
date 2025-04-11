package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.requestDTO.LoginRequest;
import org.example.highlighterdemo.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final JwtService jwtService;

    @Operation(description = "로그인을 한다.")
    @PostMapping("/api/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Validated @RequestBody LoginRequest req) {
        // JsonUsernamePasswordAuthenticationFilter 에서 처리
    }

    @Operation(description = "쿠키 확인")
    @GetMapping("/auth")
    public ResponseEntity<Boolean> auth(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @Operation(description = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletResponse response,
                                          @AuthenticationPrincipal UserDetails user) {
        try {
            jwtService.deleteTokens(user.getUsername(), response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(false);
    }
}
