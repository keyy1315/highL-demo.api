package org.example.highlighterdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.mapStruct.MemberMapper;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.model.responseDTO.MemberResponse;
import org.example.highlighterdemo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "MemberController", description = "회원 API")
public class MemberController {

    private final MemberMapper memberMapper;
    private final MemberService memberService;

    @Operation(description = "회원가입 - 사용자를 생성한다.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse setMember(@RequestBody MemberRequest req) {
        return memberMapper.toResponse(memberService.setMember(req));
    }
}
