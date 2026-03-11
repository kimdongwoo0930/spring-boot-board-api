package com.kdw.boardapi.domain.comment.dto;

import java.time.LocalDateTime;

import com.kdw.boardapi.domain.comment.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private String nickname;    // comment.getMember().getNickname()
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getMember().getNickname())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}