package com.kdw.boardapi.global.security.jwt;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // "Bearer " 제거
        // 2. 토큰 검증
        if (!jwtTokenProvider.validateToken(token)){
            filterChain.doFilter(request, response);
            return;
        }
        // 3. SecurityContext에 저장
        Long memberId = jwtTokenProvider.getMemberId(token);

        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(memberId, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 4. chain.doFilter(request, response)
        filterChain.doFilter(request, response);
    }
}
