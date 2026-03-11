package com.kdw.boardapi.domain.comment.entity;

import com.kdw.boardapi.domain.member.entity.Member;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kdw.boardapi.domain.post.entity.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)                        // not null
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)               // 필요할 때만 조회
    @JoinColumn(name = "member_id", nullable = false) // FK 컬럼명, not null
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)               // 필요할 때만 조회
    @JoinColumn(name = "post_id", nullable = false)   // FK 컬럼명, not null
    private Post post;

    @CreationTimestamp
    @Column(updatable = false)                       // 생성 후 수정 불가
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Builder.Default
    @Column(nullable = false)                        // not null
    private boolean deleted = false;

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
    }
}