package com.kdw.boardapi.domain.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdw.boardapi.domain.member.entity.Member;
import com.kdw.boardapi.domain.member.repository.MemberRepository;
import com.kdw.boardapi.domain.post.dto.PostRequest;
import com.kdw.boardapi.domain.post.dto.PostResponse;
import com.kdw.boardapi.domain.post.entity.Post;
import com.kdw.boardapi.domain.post.repository.PostRepository;
import com.kdw.boardapi.global.exception.CustomException;
import com.kdw.boardapi.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // ========== 쓰기 ==========

    /**
     * 게시글 작성
     */
    @Transactional
    public PostResponse createPost(Long memberId, PostRequest request) {
        // TODO: 1. memberId로 Member 조회
        Member member = memberRepository.findByIdAndDeletedFalse(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        // TODO: 2. Post 엔티티 생성 (member, title, content)
        Post post = Post.of(member, request);
        // TODO: 3. postRepository.save()
        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public PostResponse updatePost(Long id, PostRequest request) {
        // TODO: 1. id로 Post 조회 (deleted = false)
        Post post = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // TODO: 2. post.update() 호출
        post.update(request.getTitle(), request.getContent());
        // TODO: 3. PostResponse.from() 으로 반환
        return PostResponse.from(post);
    }

    /**
     * 게시글 삭제 (Soft Delete)
     */
    @Transactional
    public void deletePost(Long id) {
        // TODO: 1. id로 Post 조회 (deleted = false)
        Post post = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // TODO: 2. post.delete() 호출
        post.delete();
    }   

    // ========== 읽기 ==========

    /**
     * 게시글 단건 조회
     */
    @Transactional
    public PostResponse getPost(Long id) {
        // TODO: 1. id로 Post 조회 (deleted = false)
        Post post = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // TODO: 2. post.increaseViewCount() 호출 → 힌트: 읽기전용 트랜잭션에서 되는지 생각해봐
        post.increaseViewCount();
        // TODO: 3. PostResponse.from() 으로 반환
        return PostResponse.from(post);
    }

    /**
     * 게시글 목록 조회 (페이징)
     */
    public Page<PostResponse> getPosts(Pageable pageable) {
        // TODO: 1. postRepository.findAll(pageable) 또는 deleted = false 조건 추가
        Page<Post> posts = postRepository.findAllByDeletedFalse(pageable);
        // TODO: 2. map() 으로 PostResponse 변환
        return posts.map(post -> PostResponse.from(post));
    }

}
