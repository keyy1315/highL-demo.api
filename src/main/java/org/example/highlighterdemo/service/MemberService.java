package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.GameInfo;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

///     회원 관련 비즈니스 로직
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
    public Member setGameInfo(String memberId, GameInfo info) {
        Member member = memberRepository.findByUserId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "not found User Id")
        );
        member.updateGameInfo(info);
        log.info("updated gameInfo : {}", member.getGameInfo().getSummonerId());
        return member;
    }

    public List<Member> getUsers(String isActive) {
        if (isActive == null) {
            return memberRepository.findAll();
        } else if ("true".equals(isActive)) {
            return memberRepository.findAllByIsActiveTrue();
        } else if ("false".equals(isActive)) {
            return memberRepository.findAllByIsActiveFalse();
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "invalid isActive");
        }
    }

    public Member getUsersByUserId(String userId) {
        return memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "not found User Id")
        );
    }
}
