package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.highlighterdemo.config.exception.CustomException;
import org.example.highlighterdemo.config.exception.ErrorCode;
import org.example.highlighterdemo.model.entity.GameInfo;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.model.requestDTO.GameInfoRequest;
import org.example.highlighterdemo.model.requestDTO.MemberRequest;
import org.example.highlighterdemo.repository.gameInfo.GameInfoRepository;
import org.example.highlighterdemo.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

///     회원 관련 비즈니스 로직
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GameInfoRepository gameInfoRepository;

    @Transactional
    public Member signup(MemberRequest req) {
        if (memberRepository.existsById(req.userId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "exist User Id");
        }
        Member member = Member.create(req);
        try {
            memberRepository.save(member);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "회원 정보를 저장하는 중에 오류가 발생했습니다.");
        }
        return member;
    }

    @Transactional
    public Member setGameInfo(String memberId, GameInfoRequest req) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "not found User Id")
        );
        GameInfo gameInfo = GameInfo.create(req);
        if (gameInfoRepository.existsById(gameInfo.getId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "exist Game Id");
        }
        member.updateGameInfo(gameInfo);
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
        return memberRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "not found User Id")
        );
    }

    public boolean existGameInfo(String username) {
        Member member = memberRepository.findById(username).orElseThrow(() ->
                new CustomException(ErrorCode.INVALID_INPUT_VALUE, "not found User Id"));

        return member.getGameInfo() != null;
    }

    public void followMember(String userId, String username) {
        if (userId == null || username == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "you must be login or input the follow id to follow");
        }
        if (userId.equals(username)) throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "can't follow me");
        Member loginMember = memberRepository.findById(username).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "not found User Id : " + username));
        Member toFollowMember = memberRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "not found User Id : " + userId));

        loginMember.toFollowing(toFollowMember);
        toFollowMember.toFollower(loginMember);

        memberRepository.save(loginMember);
        memberRepository.save(toFollowMember);
    }

    public boolean existById(String receiver) {
        return memberRepository.existsById(receiver);
    }
}
