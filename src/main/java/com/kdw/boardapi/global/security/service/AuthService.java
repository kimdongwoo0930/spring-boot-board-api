package com.kdw.boardapi.global.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kdw.boardapi.domain.member.entity.Member;
import com.kdw.boardapi.domain.member.repository.MemberRepository;
import com.kdw.boardapi.global.exception.CustomException;
import com.kdw.boardapi.global.exception.ErrorCode;
import com.kdw.boardapi.global.security.dto.LoginRequest;
import com.kdw.boardapi.global.security.dto.TokenResponse;
import com.kdw.boardapi.global.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginRequest request) {
        // 구현
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());
        return new TokenResponse(accessToken, refreshToken);

    }
}
