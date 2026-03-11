# 📋 Spring Boot Board API

> Spring Boot 기반 게시판 REST API 프로젝트  
> JPA, MySQL을 활용한 CRUD 구현 및 실무 패턴 학습

---

## 📌 프로젝트 소개

Spring Boot를 공부하기 위한 게시판 REST API 프로젝트입니다.  
실무에서 자주 사용하는 패턴과 구조를 기반으로 작성되었습니다.

### 주요 기능

- 회원 가입 / 조회 / 수정 / 탈퇴
- 게시글 작성 / 조회 / 수정 / 삭제
- 댓글 작성 / 조회 / 수정 / 삭제
- Soft Delete (실제 삭제 없이 삭제 처리)
- 페이징 처리 (게시글 목록)
- 전역 예외처리
- 공통 응답 포맷

---

## 🛠 기술 스택

| 분류       | 기술                       |
| ---------- | -------------------------- |
| Language   | Java 25                    |
| Framework  | Spring Boot 4.0.3          |
| ORM        | Spring Data JPA, Hibernate |
| Database   | MySQL 8.0                  |
| Build Tool | Gradle                     |
| Etc        | Lombok, Swagger            |

---

## 📁 폴더 구조

```
src/main/java/com/kdw/boardapi/
├── domain/                         # 도메인별 패키지
│   ├── member/                     # 회원 도메인
│   │   ├── controller/             # API 요청/응답 처리
│   │   ├── service/                # 비즈니스 로직
│   │   ├── repository/             # DB 접근
│   │   ├── entity/                 # DB 테이블 매핑
│   │   └── dto/                    # 요청/응답 데이터 전달
│   ├── post/                       # 게시글 도메인
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   └── dto/
│   └── comment/                    # 댓글 도메인
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       └── dto/
└── global/                         # 전체 공통 패키지
    ├── exception/                  # 예외처리
    │   ├── ErrorCode.java          # 에러 코드 관리
    │   ├── CustomException.java    # 커스텀 예외
    │   └── GlobalExceptionHandler.java # 전역 예외처리
    ├── response/                   # 공통 응답
    │   └── ApiResponse.java        # 공통 응답 포맷
    └── config/                     # 설정
```

---

## 💡 주요 개념 정리

### 1. 계층 구조 (Layered Architecture)

```
Controller  →  Service  →  Repository  →  DB
```

| 계층       | 역할                                   |
| ---------- | -------------------------------------- |
| Controller | 요청/응답만 처리, 비즈니스 로직 절대 X |
| Service    | 비즈니스 로직만, DB 직접 접근 X        |
| Repository | DB 접근만 담당                         |
| Entity     | DB 테이블과 매핑, 비즈니스 로직 X      |
| DTO        | 요청/응답 데이터 전달용                |

---

### 2. 주요 어노테이션

#### Entity 어노테이션

| 어노테이션                                            | 설명                                |
| ----------------------------------------------------- | ----------------------------------- |
| `@Entity`                                             | JPA 엔티티 선언, DB 테이블과 매핑   |
| `@Table(name = "members")`                            | 매핑할 테이블명 명시                |
| `@Id`                                                 | PK 선언                             |
| `@GeneratedValue(strategy = GenerationType.IDENTITY)` | 자동으로 ID 1씩 증가                |
| `@Column(nullable = false)`                           | not null 제약조건                   |
| `@Column(unique = true)`                              | unique 제약조건                     |
| `@CreationTimestamp`                                  | 생성 시 자동으로 현재 시간 입력     |
| `@UpdateTimestamp`                                    | 수정 시 자동으로 현재 시간 업데이트 |
| `@ManyToOne(fetch = FetchType.LAZY)`                  | 다대일 연관관계, 지연 로딩          |
| `@JoinColumn(name = "member_id")`                     | FK 컬럼명 명시                      |

#### Lombok 어노테이션

| 어노테이션                 | 설명                                    |
| -------------------------- | --------------------------------------- |
| `@Getter`                  | 모든 필드 getter 자동 생성              |
| `@NoArgsConstructor`       | 파라미터 없는 기본 생성자 생성          |
| `@AllArgsConstructor`      | 모든 필드를 파라미터로 받는 생성자 생성 |
| `@Builder`                 | Builder 패턴으로 객체 생성              |
| `@RequiredArgsConstructor` | final 필드 생성자 주입                  |
| `@Slf4j`                   | 로그 사용                               |

#### Spring 어노테이션

| 어노테이션                        | 설명                              |
| --------------------------------- | --------------------------------- |
| `@RestController`                 | REST API 컨트롤러 선언            |
| `@RequestMapping`                 | URL 매핑                          |
| `@GetMapping`                     | HTTP GET 요청 매핑                |
| `@PostMapping`                    | HTTP POST 요청 매핑               |
| `@PutMapping`                     | HTTP PUT 요청 매핑                |
| `@DeleteMapping`                  | HTTP DELETE 요청 매핑             |
| `@RequestBody`                    | HTTP 요청 Body를 Java 객체로 변환 |
| `@PathVariable`                   | URL 경로 변수                     |
| `@Valid`                          | 입력값 검증                       |
| `@Service`                        | 서비스 클래스 선언                |
| `@Transactional`                  | 트랜잭션 처리                     |
| `@Transactional(readOnly = true)` | 읽기 전용 트랜잭션 (성능 최적화)  |

#### Validation 어노테이션

| 어노테이션     | 설명                           |
| -------------- | ------------------------------ |
| `@NotBlank`    | null, 빈문자열, 공백 모두 불가 |
| `@NotNull`     | null만 불가                    |
| `@Email`       | 이메일 형식 검증               |
| `@Size(min=8)` | 문자열 길이 제한               |

---

### 3. Soft Delete란?

실제로 DB에서 데이터를 삭제하지 않고 `deleted = true` 로 표시만 하는 방식

```java
// Hard Delete → DB에서 완전히 삭제 (복구 불가)
memberRepository.delete(member);

// Soft Delete → deleted = true 로 표시만 (복구 가능)
member.delete(); // deleted = true
```

**장점**

- 데이터 복구 가능
- 삭제 이력 추적 가능

---

### 4. 공통 응답 포맷

모든 API가 동일한 형태로 응답합니다.

```json
{
  "status": 200,
  "message": "성공",
  "data": {}
}
```

```java
// 성공
return ApiResponse.success(data);

// 실패
throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
```

---

### 5. FetchType.LAZY (지연 로딩)

```java
@ManyToOne(fetch = FetchType.LAZY)
private Member member;
```

- **LAZY** : 필요할 때만 DB 조회 → 성능 최적화 (실무 표준)
- **EAGER** : 즉시 DB 조회 → N+1 문제 발생 가능

---

### 6. Optional

값이 있을 수도 없을 수도 있는 경우 `null` 대신 사용

```java
// null 반환 대신
Member member = memberRepository.findByEmail(email);
member.getNickname(); // NullPointerException 위험 💀

// Optional 사용
Optional<Member> member = memberRepository.findByEmail(email);
member.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
```

---

### 7. DTO (Data Transfer Object)

|            | Request DTO                   | Response DTO                     |
| ---------- | ----------------------------- | -------------------------------- |
| 역할       | 클라이언트 → 서버 데이터 전달 | 서버 → 클라이언트 데이터 전달    |
| 어노테이션 | `@NoArgsConstructor`          | `@Builder` `@AllArgsConstructor` |
| 특징       | JSON 자동 변환                | `from()` 메서드로 Entity 변환    |

---

### 8. 페이징 (Pagination)

```java
// 요청
GET /api/v1/posts?page=0&size=10&sort=createdAt,desc

// page=0 → 1페이지 (0부터 시작)
// size=10 → 10개씩
// sort=createdAt,desc → 최신순
```

|           | Page         | Slice      |
| --------- | ------------ | ---------- |
| 전체 개수 | ✅           | ❌         |
| 용도      | 페이지네이션 | 무한스크롤 |

---

## 📝 API 명세

### 회원 API

| Method | URL                  | 설명           |
| ------ | -------------------- | -------------- |
| POST   | /api/v1/members      | 회원가입       |
| GET    | /api/v1/members/{id} | 회원 단건 조회 |
| PATCH  | /api/v1/members/{id} | 회원 정보 수정 |
| DELETE | /api/v1/members/{id} | 회원 탈퇴      |

### 게시글 API

| Method | URL                | 설명                      |
| ------ | ------------------ | ------------------------- |
| POST   | /api/v1/posts      | 게시글 작성               |
| GET    | /api/v1/posts      | 게시글 목록 조회 (페이징) |
| GET    | /api/v1/posts/{id} | 게시글 단건 조회          |
| PATCH  | /api/v1/posts/{id} | 게시글 수정               |
| DELETE | /api/v1/posts/{id} | 게시글 삭제               |

### 댓글 API

| Method | URL                                  | 설명           |
| ------ | ------------------------------------ | -------------- |
| POST   | /api/v1/posts/{postId}/comments      | 댓글 작성      |
| GET    | /api/v1/posts/{postId}/comments      | 댓글 목록 조회 |
| PATCH  | /api/v1/posts/{postId}/comments/{id} | 댓글 수정      |
| DELETE | /api/v1/posts/{postId}/comments/{id} | 댓글 삭제      |
