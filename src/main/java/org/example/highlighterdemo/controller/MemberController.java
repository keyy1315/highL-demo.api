package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.model.responseDTO.MemberResponse;
import org.example.highlighterdemo.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "MemberController", description = "회원 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(description = "회원가입 - 사용자를 생성한다.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse signup(@RequestBody MemberRequest req) {
        return MemberResponse.create(memberService.signup(req));
    }

    @Operation(description = "회원 정보 수정 - 라이엇 계정 아이디와 티어를 연동한다.")
    @PatchMapping("/lol")
    public MemberResponse patchGameId(@RequestBody MemberRequest req) {
        return MemberResponse.create(memberService.patchGameId(req));
    }

    @Operation(description = "전체 회원 목록 조회")
    @GetMapping
    public List<MemberResponse> getUsers(@RequestParam(value = "isActive", required = false)
                                         @Parameter(description = "true/false - 활성화/비활성화")
                                         String isActive) {
        return memberService.getUsers(isActive).stream()
                .map(MemberResponse::create)
                .collect(Collectors.toList());
    }

    @Operation(description = "회원 ID로 회원 조회")
    @GetMapping("/{userId}")
    public MemberResponse getUsersByUserId(@PathVariable String userId) {
        return MemberResponse.create(memberService.getUsersByUserId(userId));
    }
}
