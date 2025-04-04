package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.requestDTO.GameInfoRequest;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.model.responseDTO.MemberResponse;
import org.example.highlighterdemo.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    public MemberResponse patchGameId(@RequestBody GameInfoRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null){
            throw new CustomException(ErrorCode.UNAUTHORIZED, "no login information");
        }
        if(memberService.existGameInfo(userDetails.getUsername())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, userDetails.getUsername() + " is already has GameName..");
        }
        return MemberResponse.create(memberService.setGameInfo(userDetails.getUsername(), request));
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

    @Operation(description = "로그인 한 회원 정보 조회")
    @GetMapping("/get")
    public MemberResponse getUser(@AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) return null;
        return MemberResponse.create(memberService.getUsersByUserId(userDetails.getUsername()));
    }

    @Operation(description = "팔로우")
    @PatchMapping("/follow/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void followMember(@PathVariable String userId, @AuthenticationPrincipal UserDetails userDetails) {
        memberService.followMember(userId, userDetails.getUsername());
    }
}
