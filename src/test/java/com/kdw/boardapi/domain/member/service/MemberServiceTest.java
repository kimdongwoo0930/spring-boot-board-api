package com.kdw.boardapi.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kdw.boardapi.domain.member.dto.MemberCreateRequest;
import com.kdw.boardapi.domain.member.dto.MemberResponse;
import com.kdw.boardapi.domain.member.dto.MemberUpdateRequest;
import com.kdw.boardapi.domain.member.entity.Member;
import com.kdw.boardapi.domain.member.repository.MemberRepository;
import com.kdw.boardapi.global.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원가입_성공() {
        // given
        MemberCreateRequest request = new MemberCreateRequest("test@test.com", "password1234", "테스터");

        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        MemberResponse response = memberService.join(request);

        // then
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getNickname()).isEqualTo("테스터");
    }

    /**
     * 순서
     * 테스트할 객체를 생성
     * 데이터 준비)
     * given -> 테스트 환경 세팅
     */

    @Test
    void 회원조회_성공() {

        // given
        Long memberId = 12L;

        Member member = Member.builder()
            .email("test@test.com")
            .nickname("테스터")
            .password("encodedPassword")
            .build();

        given(memberRepository.findByIdAndDeletedFalse(memberId)).willReturn(Optional.of(member));
        // when
        MemberResponse response = memberService.getMember(memberId);
        // then
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getNickname()).isEqualTo("테스터");
    }

    @Test
    void 회원조회_실패_존재하지않는ID() {
        // given
        Long memberId = 999L;
        given(memberRepository.findByIdAndDeletedFalse(memberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.getMember(memberId))
            .isInstanceOf(CustomException.class);
    }

    @Test
    void 회원수정_성공() {
        // given
        Long memberId = 999L;

        Member member = Member.builder()
            .email("test@test.com")
            .nickname("테스터")
            .password("encodedPassword")
            .build();
        given(memberRepository.findByIdAndDeletedFalse(memberId)).willReturn(Optional.of(member));

        MemberUpdateRequest request = new MemberUpdateRequest("테스터1");
        // when
        MemberResponse response = memberService.updateMember(memberId, request);
        
        // then
        assertThat(response.getNickname()).isEqualTo("테스터1");
    }

    @Test
    void 회원탈퇴_성공() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
            .email("test@test.com")
            .nickname("테스터")
            .password("encodedPassword")
            .build();
        given(memberRepository.findByIdAndDeletedFalse(memberId)).willReturn(Optional.of(member));

        // when
        memberService.deleteMember(memberId);

        // then
        assertThat(member.isDeleted()).isTrue();
    }

    @Test
    void 회원탈퇴_실패_존재하지않는ID() {
        // given
        Long memberId = 999L;
        given(memberRepository.findByIdAndDeletedFalse(memberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.deleteMember(memberId))
            .isInstanceOf(CustomException.class);
    }
}