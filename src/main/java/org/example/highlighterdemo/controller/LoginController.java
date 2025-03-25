package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.requestDTO.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class LoginController {
    @Operation(description = "로그인을 한다.")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Validated @RequestBody LoginRequest req) {
        // JsonUsernamePasswordAuthenticationFilter 에서 처리
    }
}
