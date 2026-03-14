package com.kdw.boardapi.domain.member.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kdw.boardapi.domain.member.dto.MemberCreateRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity                                              // JPA 엔티티 선언 (DB 테이블과 매핑)
@Table(name = "members")                             // 매핑할 테이블명 명시
@Getter                                              // 모든 필드 getter 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // 기본 생성자 protected로 외부 생성 막기
@Builder                                             // Builder 패턴으로 객체 생성
@AllArgsConstructor                                  // Builder와 함께 사용하기 위한 전체 생성자
public class Member {

    @Id                                              // PK 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment (DB가 자동으로 증가)
    private Long id;

    @Column(nullable = false, unique = true)         // not null, 중복 불가
    private String email;

    @Column(nullable = false)                        // not null
    private String password;

    @Column(nullable = false)                        // not null
    private String nickname;

    @CreationTimestamp                               // 생성 시 자동으로 현재 시간 입력
    @Column(updatable = false)                       // 생성 후 수정 불가
    private LocalDateTime createdAt;

    @UpdateTimestamp                                 // 수정 시 자동으로 현재 시간 업데이트
    private LocalDateTime updatedAt;

    @Builder.Default                                 // Builder 사용 시 기본값 적용
    @Column(nullable = false)                        // not null
    private boolean deleted = false;                 // 기본값 false (Soft Delete용)

    /**
     * 회원 정보 수정
     * setter 대신 의미있는 메서드명 사용 (실무 표준)
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 회원 탈퇴 (Soft Delete)
     * DB에서 실제로 삭제하지 않고 deleted = true 로 표시
     */
    public void delete() {
        this.deleted = true;
    }

    
    public static Member of(MemberCreateRequest request, String password) {
    return Member.builder()
            .email(request.getEmail())
            .password(password)
            .nickname(request.getNickname())
            .build();
    }
}