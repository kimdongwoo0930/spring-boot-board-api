package com.kdw.boardapi.domain.post.dto;

import java.time.LocalDateTime;

import com.kdw.boardapi.domain.post.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String nickname;    // post.getMember().getNickname()
    private int viewCount;
    private LocalDateTime createdAt;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(post.getMember().getNickname())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}