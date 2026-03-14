# Spring Security + JWT 구현 문서

## 개요

- Spring Security와 JWT를 이용한 인증/인가 구현
- 브랜치: `feature/security`

---

## 의존성 추가

```gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'
```

---

## 구조

```
security/
├── config/
│   ├── SecurityConfig.java          # Security 설정 (FilterChain, URL 권한)
│   └── SwaggerConfig.java           # Swagger JWT 인증 버튼 설정
├── jwt/
│   ├── JwtTokenProvider.java        # JWT 생성 / 검증 / 파싱
│   └── JwtAuthenticationFilter.java # 요청마다 JWT 검사하는 필터
├── service/
│   └── AuthService.java             # 로그인, 토큰 발급
├── dto/
│   ├── LoginRequest.java
│   └── TokenResponse.java
└── AuthController.java              # POST /api/v1/auth/login
```

---

## 인증 흐름

```
1. 로그인 요청 (POST /api/v1/auth/login)
        ↓
2. AuthService → DB에서 회원 조회 (email)
        ↓
3. BCrypt로 비밀번호 검증
        ↓
4. 인증 성공 시 JWT 발급 (AccessToken + RefreshToken)
        ↓
5. 이후 요청 시 Header에 Bearer 토큰 포함
        ↓
6. JwtAuthenticationFilter → 토큰 검증 → SecurityContext에 인증 정보 저장
        ↓
7. Controller에서 @AuthenticationPrincipal Long memberId 로 꺼내서 사용
```

---

## API 접근 권한

| 엔드포인트                                  | 권한              |
| ------------------------------------------- | ----------------- |
| POST /api/v1/auth/login                     | 누구나            |
| POST /api/v1/members                        | 누구나 (회원가입) |
| GET /api/v1/posts                           | 누구나            |
| GET /api/v1/posts/{id}                      | 누구나            |
| POST /api/v1/posts                          | 로그인 필요       |
| PATCH /api/v1/posts/{id}                    | 로그인 필요       |
| DELETE /api/v1/posts/{id}                   | 로그인 필요       |
| POST /api/v1/posts/{postId}/comments        | 로그인 필요       |
| PATCH /api/v1/posts/{postId}/comments/{id}  | 로그인 필요       |
| DELETE /api/v1/posts/{postId}/comments/{id} | 로그인 필요       |

---

## JWT 구조

### AccessToken

- 유효기간: 30분 (1800000ms)
- 용도: API 인증

### RefreshToken

- 유효기간: 7일 (604800000ms)
- 용도: AccessToken 재발급 (현재 미구현 - 추후 개선)

---

## 구현 순서

- [x] 1. 의존성 추가
- [x] 2. `JwtTokenProvider` 구현
- [x] 3. `JwtAuthenticationFilter` 구현
- [x] 4. `SecurityConfig` 구현
- [x] 5. `AuthService` / `AuthController` 구현 (로그인, 토큰 발급)
- [x] 6. 비밀번호 BCrypt 암호화 적용
- [x] 7. 기존 Controller `@AuthenticationPrincipal`로 memberId 수신

---

## JwtAuthenticationFilter 흐름

```
HTTP 요청
    ↓
JwtAuthenticationFilter.doFilterInternal()
    ↓
1. Header 추출
   request.getHeader("Authorization")
   → "Bearer eyJhbGci..." 형태
   → null 이거나 "Bearer "로 시작 안 하면 → 그냥 다음 필터로 넘김
    ↓
2. 토큰 파싱
   token = header.substring(7)  // "Bearer " 7글자 제거
    ↓
3. 토큰 검증
   jwtTokenProvider.validateToken(token)
   → 유효하지 않으면 → 그냥 다음 필터로 넘김 (인증 없이 진행)
    ↓
4. memberId 추출
   jwtTokenProvider.getMemberId(token)
    ↓
5. SecurityContext에 인증 정보 저장
   UsernamePasswordAuthenticationToken authentication =
       new UsernamePasswordAuthenticationToken(memberId, null, List.of())
   SecurityContextHolder.getContext().setAuthentication(authentication)
    ↓
6. 다음 필터로 전달
   filterChain.doFilter(request, response)
```

### SecurityContext란?

- Spring Security가 현재 요청의 인증 정보를 보관하는 공간
- 여기에 저장해두면 Controller에서 `@AuthenticationPrincipal`로 꺼낼 수 있음
- 요청이 끝나면 자동으로 비워짐

### Bearer 토큰이란?

- HTTP Authorization 헤더 형식: `Authorization: Bearer {token}`
- "Bearer"는 토큰 타입을 나타내는 접두사

---

## JwtTokenProvider 메서드 정리

| 메서드                                | 역할                                       |
| ------------------------------------- | ------------------------------------------ |
| `generateAccessToken(Long memberId)`  | AccessToken 생성 (유효기간 30분)           |
| `generateRefreshToken(Long memberId)` | RefreshToken 생성 (유효기간 7일)           |
| `validateToken(String token)`         | 토큰 서명/만료 검증 → true/false           |
| `getMemberId(String token)`           | 토큰 payload에서 memberId 추출             |
| `getSigningKey()`                     | secretKey String → SecretKey 변환 (내부용) |

---

## application.properties 설정

```properties
# JWT
jwt.secret=boardapi-secret-key-must-be-at-least-256-bits-long-for-security
jwt.access-expiration=1800000
jwt.refresh-expiration=604800000
```

---

## 변경사항

### MemberService

- `passwordEncoder.encode()` 로 비밀번호 암호화 후 저장

### PostController / CommentController

- `@RequestParam Long memberId` → `@AuthenticationPrincipal Long memberId` 로 변경
- JWT 토큰에서 자동으로 추출
