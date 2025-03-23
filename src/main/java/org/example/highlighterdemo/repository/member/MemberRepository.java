package org.example.highlighterdemo.repository.member;

import org.example.highlighterdemo.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
///     저장소...
///     JPA 사용함
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByRefreshToken(String refreshToken);

    List<Member> findAllByIsActiveTrue();

    List<Member> findAllByIsActiveFalse();
}
