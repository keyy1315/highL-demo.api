package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.mapStruct.MemberMapper;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

///     회원 관련 비즈니스 로직
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;

    ///     회원가입 메소드 (미완성)
    @Transactional
    public Member setMember(MemberRequest req) {
        if(memberRepository.existsByUserId(req.userId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "exist User Id");
        }
        Member member = memberMapper.toMember(req);
        memberRepository.save(member);
        return member;
    }

}
