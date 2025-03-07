package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.entity.Member;
import org.example.highlighterdemo.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
///     로그인 처리 관련 비즈니스 로직
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    ///     토큰에서 추출한 userId 값을 통해 UserDetails 객체를 생성한다.
    ///     이렇게 생성된 UserDetails 객체는 Principal 에 저장되어
    ///     사용자가 재로그인 하지 않아도 토큰 만료 기간 동안은 사용자의 정보를 서버에서 가져올 수 있다.
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member
                = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId));

        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }
}
