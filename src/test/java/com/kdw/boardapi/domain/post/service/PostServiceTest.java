package com.kdw.boardapi.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kdw.boardapi.domain.member.entity.Member;
import com.kdw.boardapi.domain.member.repository.MemberRepository;
import com.kdw.boardapi.domain.post.dto.PostRequest;
import com.kdw.boardapi.domain.post.dto.PostResponse;
import com.kdw.boardapi.domain.post.entity.Post;
import com.kdw.boardapi.domain.post.repository.PostRepository;
import com.kdw.boardapi.global.exception.CustomException;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 게시글작성_성공() {
        // given - memberId, PostRequest 준비
        //       - memberRepository.findByIdAndDeletedFalse() Mock 설정
        //       - postRepository.save() Mock 설정
        Long memberId = 999L;
        Member member = Member.builder()
            .email("test@test.com")
            .nickname("테스터")
            .password("encodedPassword")
            .build();

        PostRequest request = new PostRequest("제목","내용");
        given(memberRepository.findByIdAndDeletedFalse(memberId)).willReturn(Optional.of(member));
        given(postRepository.save(any(Post.class))).willAnswer(invocation -> invocation.getArgument(0));
        // when - postService.createPost() 호출
        PostResponse response = postService.createPost(memberId, request);
        // then - response.getTitle(), response.getContent() 검증
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
    }

    @Test
    void 게시글작성_실패_존재하지않는회원() {
        // given - 없는 memberId
        //       - memberRepository.findByIdAndDeletedFalse() → Optional.empty() 반환
        Long memberId = 998L;
        given(memberRepository.findByIdAndDeletedFalse(memberId)).willReturn(Optional.empty());

        // when & then - CustomException 발생 검증
        assertThatThrownBy(() -> postService.createPost(memberId,new PostRequest("제목","내용")))
            .isInstanceOf(CustomException.class);
    }

    @Test
    void 게시글조회_성공() {
        // given - postId 준비
        //       - postRepository.findByIdAndDeletedFalse() Mock 설정
        Long postId = 999L;
        Member member = Member.builder()
            .email("test@test.com")
            .nickname("테스터")
            .password("encodedPassword")
            .build();

        Post post = Post.builder()
            .title("제목")
            .content("내용")
            .member(member)
            .build();

        given(postRepository.findByIdAndDeletedFalse(postId)).willReturn(Optional.of(post));

        // when
        PostResponse response = postService.getPost(postId);

        // then
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
    }

    @Test
    void 게시글조회_실패_존재하지않는ID() {
        // given
        Long postId = 999L;
        given(postRepository.findByIdAndDeletedFalse(postId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.getPost(postId))
            .isInstanceOf(CustomException.class);
    }

    @Test
    void 게시글수정_성공() {
        // given
        Long postId = 1L;
        Member member = Member.builder()
            .email("test@test.com")
            .nickname("테스터")
            .password("encodedPassword")
            .build();
        Post post = Post.builder()
            .title("제목")
            .content("내용")
            .member(member)
            .build();
        given(postRepository.findByIdAndDeletedFalse(postId)).willReturn(Optional.of(post));

        // when
        PostResponse response = postService.updatePost(postId, new PostRequest("수정된제목", "수정된내용"));

        // then
        assertThat(response.getTitle()).isEqualTo("수정된제목");
        assertThat(response.getContent()).isEqualTo("수정된내용");
    }

    @Test
    void 게시글삭제_성공() {
        // given
        Long postId = 1L;
        Member member = Member.builder()
            .email("test@test.com")
            .nickname("테스터")
            .password("encodedPassword")
            .build();
        Post post = Post.builder()
            .title("제목")
            .content("내용")
            .member(member)
            .build();
        given(postRepository.findByIdAndDeletedFalse(postId)).willReturn(Optional.of(post));

        // when
        postService.deletePost(postId);

        // then
        assertThat(post.isDeleted()).isTrue();
    }
}
