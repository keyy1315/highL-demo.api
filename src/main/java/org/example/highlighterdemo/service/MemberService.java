package org.example.highlighterdemo.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.model.responseDTO.MemberResponse;
import org.example.highlighterdemo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

///     회원 관련 비즈니스 로직
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    ///     회원가입 메소드 (미완성)
    @Transactional
    public Member signup(MemberRequest req) {
        if (memberRepository.existsByUserId(req.userId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "exist User Id");
        }
        Member member = Member.create(req);
        try {
            memberRepository.save(member);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "error while saving member");
        }
        return member;
    }

    @Transactional
    public Member patchGameId(MemberRequest req) {
        Member member = memberRepository.findByUserId(req.userId()).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "not found User Id")
        );
        try {
            member = member.addGameId(req.tier(),req.userName(), req.nameTag());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "error while updating member");
        }
        return member;
    }

    public List<Member> getUsers(String isActive) {
        if(isActive == null) {
            return memberRepository.findAll();
        } else if("true".equals(isActive)) {
            return memberRepository.findAllByActiveTrue();
        } else if("false".equals(isActive)) {
            return memberRepository.findAllByActiveFalse();
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "invalid isActive");
        }
    }
}
