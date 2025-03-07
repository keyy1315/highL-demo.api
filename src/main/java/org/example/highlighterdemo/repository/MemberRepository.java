package org.example.highlighterdemo.repository;

import org.example.highlighterdemo.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
///     저장소...
///     JPA 사용함
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUserId(String userId);

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByRefreshToken(String refreshToken);
}
