package com.kdw.boardapi.domain.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdw.boardapi.domain.comment.dto.CommentRequest;
import com.kdw.boardapi.domain.comment.dto.CommentResponse;
import com.kdw.boardapi.domain.comment.entity.Comment;
import com.kdw.boardapi.domain.comment.repository.CommentRepository;
import com.kdw.boardapi.domain.member.entity.Member;
import com.kdw.boardapi.domain.member.repository.MemberRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // ========== 쓰기 ==========

    /**
     * 댓글 작성
     *
     * @param postId   댓글이 달릴 게시글 ID
     * @param memberId 작성자 회원 ID
     * @param request  {@link CommentRequest} 댓글 작성 요청 DTO
     * @return 생성된 댓글 정보
     */
    // TODO: createComment(Long postId, Long memberId, CommentRequest request)
    @Transactional
    public CommentResponse createComment(Long postId, Long memberId, CommentRequest request){
        // 1. postId로 Post 조회 (deleted = false) → POST_NOT_FOUND
        Post post = postRepository.findByIdAndDeletedFalse(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // 2. memberId로 Member 조회 (deleted = false) → MEMBER_NOT_FOUND
        Member member = memberRepository.findByIdAndDeletedFalse(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        // 3. Comment.builder()로 엔티티 생성 (post, member, content)
        Comment comment = Comment.of(request, member, post);
        // 4. commentRepository.save()
        commentRepository.save(comment);
        // 5. CommentResponse.from()으로 반환
        return CommentResponse.from(comment);
    }


    /**
     * 댓글 수정
     *
     * @param id      댓글 ID
     * @param request {@link CommentRequest} 댓글 수정 요청 DTO
     * @return 수정된 댓글 정보
     */
    // TODO: updateComment(Long id, CommentRequest request)
    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest request){
        // 1. id로 Comment 조회 (deleted = false) → COMMENT_NOT_FOUND
        Comment comment = commentRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        // 2. comment.update() 호출
        comment.update(request.getContent());
        // 3. CommentResponse.from()으로 반환
        return CommentResponse.from(comment);
    }


    /**
     * 댓글 삭제 (Soft Delete)
     *
     * @param id 댓글 ID
     */
    // TODO: deleteComment(Long id)
    @Transactional
    public void deleteComment(Long id){
        // 1. id로 Comment 조회 (deleted = false) → COMMENT_NOT_FOUND
        Comment comment = commentRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        // 2. comment.delete() 호출
        comment.delete();
    }


    // ========== 읽기 ==========

    /**
     * 게시글의 댓글 목록 조회
     *
     * @param postId 게시글 ID
     * @return 댓글 목록 (삭제되지 않은 것만)
     */
    // TODO: getComments(Long postId)
    public List<CommentResponse> getComments(Long postId){
        // 1. commentRepository.findAllByPostIdAndDeletedFalse(postId)
        List<Comment> comments = commentRepository.findAllByPostIdAndDeletedFalse(postId);
        // 2. stream().map()으로 CommentResponse 변환 후 List로 반환
        return comments.stream()
        .map(CommentResponse::from)
        .collect(Collectors.toList());
    }


}
