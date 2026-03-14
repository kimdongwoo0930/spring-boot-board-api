package com.kdw.boardapi.domain.post.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kdw.boardapi.domain.member.entity.Member;
import com.kdw.boardapi.domain.post.dto.PostRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)  // LAZY = 필요할 때만 조회 (실무 표준)
    @JoinColumn(name = "member_id")     // FK 컬럼명 명시   
    private Member member;

    @Builder.Default
    @Column(nullable = false)
    private int viewCount = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean deleted = false;

    /**
     * 게시글 정보 수정
     * setter 대신 의미있는 메서드명 사용 (실무 표준)
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * 게시글 삭제 (Soft Delete)
     * DB에서 실제로 삭제하지 않고 deleted = true 로 표시
     */
    public void delete() {
        this.deleted = true;
    }

    /**
    * 조회수 증가
    */
   public void increaseViewCount(){
    this.viewCount++;
   }

   public static Post of(Member member, PostRequest request) {
    return Post.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .member(member)
            .build();
}
}
