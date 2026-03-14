package com.kdw.boardapi.domain.member.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdw.boardapi.domain.member.dto.MemberCreateRequest;
import com.kdw.boardapi.domain.member.dto.MemberResponse;
import com.kdw.boardapi.domain.member.dto.MemberUpdateRequest;
import com.kdw.boardapi.domain.member.entity.Member;
import com.kdw.boardapi.domain.member.repository.MemberRepository;
import com.kdw.boardapi.global.exception.CustomException;
import com.kdw.boardapi.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 서비스
 *
 * @author kdw
 * @since 2026.03.11
 */
@Slf4j
@Service
@Transactional(readOnly = true)  // 기본 읽기 전용
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // ========== 쓰기 ==========

    /**
     * 회원가입
     */
    @Transactional
    public MemberResponse join(MemberCreateRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = Member.of(request, encodedPassword);
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateNickname(request.getNickname());
        return MemberResponse.from(member);
    }

    /**
     * 회원 탈퇴 (Soft Delete)
     */
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.delete();
    }

    // ========== 읽기 ==========

    /**
     * 회원 단건 조회
     */
    public MemberResponse getMember(Long id) {
        Member member = memberRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberResponse.from(member);
    }
}

