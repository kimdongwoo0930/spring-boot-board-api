package com.kdw.boardapi.domain.member.repository;

import java.util.Optional;
import com.kdw.boardapi.domain.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

// <엔티티, PK타입>
public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Member> findByIdAndDeletedFalse(Long id);
}
