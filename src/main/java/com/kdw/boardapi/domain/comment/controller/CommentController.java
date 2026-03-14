package com.kdw.boardapi.domain.comment.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdw.boardapi.domain.comment.dto.CommentRequest;
import com.kdw.boardapi.domain.comment.dto.CommentResponse;
import com.kdw.boardapi.domain.comment.service.CommentService;
import com.kdw.boardapi.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     * POST /api/v1/posts/{postId}/comments?memberId={memberId}
     *
     * @param postId   댓글이 달릴 게시글 ID
     * @param memberId 작성자 회원 ID (임시: JWT 적용 전 쿼리 파라미터로 수신)
     * @param request  {@link CommentRequest} 댓글 내용
     * @return 201 Created, 생성된 댓글 정보
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
        @PathVariable Long postId,
        @AuthenticationPrincipal Long memberId,
        @RequestBody @Valid CommentRequest request){
        return ResponseEntity.status(201).body(ApiResponse.success(commentService.createComment(postId, memberId, request)));
    }

    /**
     * 게시글의 댓글 목록 조회
     * GET /api/v1/posts/{postId}/comments
     *
     * @param postId 게시글 ID
     * @return 200 OK, 삭제되지 않은 댓글 목록
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable Long postId) {
        return ResponseEntity.status(200).body(ApiResponse.success(commentService.getComments(postId)));
    }

    /**
     * 댓글 수정
     * PATCH /api/v1/posts/{postId}/comments/{id}
     *
     * @param postId  게시글 ID
     * @param id      수정할 댓글 ID
     * @param request {@link CommentRequest} 수정할 댓글 내용
     * @return 200 OK, 수정된 댓글 정보
     */
    @PatchMapping("/{postId}/comments/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
        @PathVariable Long postId,
        @PathVariable Long id,
        @RequestBody @Valid CommentRequest request) {
        return ResponseEntity.status(200).body(ApiResponse.success(commentService.updateComment(id, request)));
    }

    /**
     * 댓글 삭제 (Soft Delete)
     * DELETE /api/v1/posts/{postId}/comments/{id}
     *
     * @param postId 게시글 ID
     * @param id     삭제할 댓글 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{postId}/comments/{id}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long postId,
        @PathVariable Long id) {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        }
}
