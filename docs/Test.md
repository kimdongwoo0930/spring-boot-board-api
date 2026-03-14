# 단위 테스트 (Unit Test) 구현 문서

## 개요

- JUnit5 + Mockito를 이용한 단위 테스트 구현
- 브랜치: `feature/test`

---

## 의존성

Spring Boot 기본 제공 (별도 추가 불필요)

```gradle
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

> `spring-boot-starter-test` 안에 JUnit5, Mockito, AssertJ 모두 포함되어 있음

---

## 단위 테스트 vs 통합 테스트

| 구분 | 단위 테스트 | 통합 테스트 |
| ---- | ----------- | ----------- |
| 대상 | 클래스 하나 (Service 등) | 전체 애플리케이션 |
| DB | Mock (가짜) | 실제 DB 사용 |
| 속도 | 빠름 | 느림 |
| 어노테이션 | `@ExtendWith(MockitoExtension.class)` | `@SpringBootTest` |
| 용도 | 비즈니스 로직 검증 | API 전체 흐름 검증 |

---

## 핵심 어노테이션

| 어노테이션 | 위치 | 설명 |
| ---------- | ---- | ---- |
| `@ExtendWith(MockitoExtension.class)` | 클래스 | JUnit5에 Mockito 연결 |
| `@InjectMocks` | 필드 | 테스트 대상 클래스. `@Mock` 객체들을 자동 주입 |
| `@Mock` | 필드 | 가짜 객체 생성 (DB, 외부 의존성 등) |
| `@Test` | 메서드 | 테스트 메서드 선언 |

---

## Mock이란?

실제 DB나 외부 서비스 없이 **가짜 객체**를 만들어 테스트하는 방식

```
실제 환경:  MemberService → MemberRepository → DB
테스트 환경: MemberService → MemberRepository(Mock) → 가짜 응답
```

- Mock 객체는 실제로 동작하지 않음
- `given()`으로 동작을 직접 설정해줘야 함
- DB 없이도 서비스 로직만 순수하게 테스트 가능

---

## given / when / then 패턴

```java
@Test
void 회원가입_성공() {
    // given - 테스트 데이터 준비 + Mock 동작 설정
    MemberCreateRequest request = new MemberCreateRequest("test@test.com", "password1234", "테스터");

    given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
    given(memberRepository.save(any(Member.class))).willAnswer(invocation -> invocation.getArgument(0));

    // when - 테스트할 메서드 실행
    MemberResponse response = memberService.join(request);

    // then - 결과 검증
    assertThat(response.getEmail()).isEqualTo("test@test.com");
    assertThat(response.getNickname()).isEqualTo("테스터");
}
```

| 단계 | 역할 |
| ---- | ---- |
| given | 테스트 데이터 준비, Mock 동작 설정 |
| when | 실제 테스트할 메서드 호출 (딱 한 번) |
| then | 결과 검증 |

---

## 주요 Mock 설정 문법

### willReturn - 고정값 반환

```java
given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
// encode()가 호출되면 항상 "encodedPassword" 반환
```

### willAnswer - 받은 인자를 가공해서 반환

```java
given(memberRepository.save(any(Member.class))).willAnswer(invocation -> invocation.getArgument(0));
// save()에 넘긴 Member 객체를 그대로 반환 (실제 DB 저장 없이)
```

### willThrow - 예외 던지기

```java
given(memberRepository.findByIdAndDeletedFalse(anyLong())).willReturn(Optional.empty());
// 빈 Optional 반환 → orElseThrow() 에서 CustomException 발생
```

---

## 인자 매처 (ArgumentMatchers)

| 매처 | 의미 |
| ---- | ---- |
| `anyString()` | 어떤 문자열이든 |
| `anyLong()` | 어떤 Long이든 |
| `any(Member.class)` | Member 타입이면 어떤 객체든 |
| `eq("정확한값")` | 정확히 이 값일 때만 |

---

## 검증 문법 (AssertJ)

```java
assertThat(response.getEmail()).isEqualTo("test@test.com");  // 값이 같은지
assertThat(response).isNotNull();                            // null이 아닌지
assertThat(list).hasSize(3);                                 // 리스트 크기
assertThatThrownBy(() -> memberService.join(request))        // 예외 발생 검증
    .isInstanceOf(CustomException.class);
```

---

## 테스트 케이스 종류

서비스 메서드 하나당 보통 아래 2가지를 작성

| 케이스 | 설명 |
| ------ | ---- |
| `메서드_성공` | 정상 흐름 검증 |
| `메서드_실패` | 예외 발생 케이스 검증 (없는 ID, 잘못된 값 등) |

---

## 케이스별 패턴 정리

### 성공 케이스

```java
@Test
void 게시글조회_성공() {
    // given
    Long postId = 1L;
    Post post = Post.builder().title("제목").content("내용").member(member).build();
    given(postRepository.findByIdAndDeletedFalse(postId)).willReturn(Optional.of(post));

    // when
    PostResponse response = postService.getPost(postId);

    // then
    assertThat(response.getTitle()).isEqualTo("제목");
}
```

### 실패 케이스 (예외 발생)

```java
@Test
void 게시글조회_실패_존재하지않는ID() {
    // given
    Long postId = 999L;
    given(postRepository.findByIdAndDeletedFalse(postId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> postService.getPost(postId))
        .isInstanceOf(CustomException.class);
}
```

### 삭제 케이스 (반환값 없음)

```java
@Test
void 게시글삭제_성공() {
    // given
    Long postId = 1L;
    Post post = Post.builder().title("제목").content("내용").member(member).build();
    given(postRepository.findByIdAndDeletedFalse(postId)).willReturn(Optional.of(post));

    // when
    postService.deletePost(postId);

    // then
    assertThat(post.isDeleted()).isTrue();  // 반환값 없으므로 객체 상태 직접 확인
}
```

### save() Mock 설정 (DB 저장 없이 받은 객체 그대로 반환)

```java
given(postRepository.save(any(Post.class))).willAnswer(invocation -> invocation.getArgument(0));
```

---

## DTO 객체 생성 팁

테스트에서 DTO를 쉽게 생성하려면 `@AllArgsConstructor` 추가

```java
// DTO에 추가
@NoArgsConstructor
@AllArgsConstructor  // 추가
public class MemberCreateRequest { ... }

// 테스트에서 한 줄로 생성 가능
MemberCreateRequest request = new MemberCreateRequest("test@test.com", "password1234", "테스터");
```

---

## 구조

```
src/test/java/com/kdw/boardapi/
└── domain/
    ├── member/
    │   └── service/
    │       └── MemberServiceTest.java
    └── post/
        └── service/
            └── PostServiceTest.java
```
