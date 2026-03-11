package com.kdw.boardapi.domain.member.dto;

import java.time.LocalDateTime;

import com.kdw.boardapi.domain.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private Long id;
    private String email;
    private String nickname;

    private LocalDateTime createdAt;
    

    // @Builder를 추가해야 from() 안에 builder() 사용 가능
    public static MemberResponse from(Member member){
        return MemberResponse.builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .createdAt(member.getCreatedAt())
        .build();
    }
}
