package com.kdw.boardapi.domain.post.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.kdw.boardapi.domain.post.dto.PostRequest;
import com.kdw.boardapi.domain.post.dto.PostResponse;
import com.kdw.boardapi.domain.post.service.PostService;
import com.kdw.boardapi.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 작성
     *
     * @param memberId 작성자 회원 ID (쿼리 파라미터)
     * @param request  {@link PostRequest} 게시글 작성 요청 DTO
     * @return 생성된 게시글 정보 (201 Created)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@AuthenticationPrincipal Long memberId, @RequestBody @Valid PostRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.success(postService.createPost(memberId, request)));
    }

    /**
     * 게시글 목록 조회 (페이징)
     *
     * @param pageable 페이징 정보 (?page=0&size=10&sort=createdAt,desc)
     * @return 게시글 목록 페이지 (200 OK)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPosts(Pageable pageable) {
        return ResponseEntity.status(200).body(ApiResponse.success(postService.getPosts(pageable)));
    }

    /**
     * 게시글 단건 조회 (조회수 증가)
     *
     * @param id 게시글 ID
     * @return 게시글 정보 (200 OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        return ResponseEntity.status(200).body(ApiResponse.success(postService.getPost(id)));
    }

    /**
     * 게시글 수정
     *
     * @param id      게시글 ID
     * @param request {@link PostRequest} 게시글 수정 요청 DTO
     * @return 수정된 게시글 정보 (200 OK)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequest request) {
        return ResponseEntity.status(200).body(ApiResponse.success(postService.updatePost(id, request)));
    }

    /**
     * 게시글 삭제 (Soft Delete)
     *
     * @param id 게시글 ID
     * @return 응답 바디 없음 (204 No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
    
    
    

}
