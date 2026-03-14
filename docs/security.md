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
│   └── SecurityConfig.java         # Security 설정 (FilterChain, CORS 등)
├── jwt/
│   ├── JwtTokenProvider.java       # JWT 생성 / 검증
│   └── JwtAuthenticationFilter.java # 요청마다 JWT 검사하는 필터
└── service/
    └── CustomUserDetailsService.java # DB에서 유저 조회
```

---

## 인증 흐름

```
1. 로그인 요청 (POST /api/v1/auth/login)
        ↓
2. CustomUserDetailsService → DB에서 회원 조회
        ↓
3. 인증 성공 시 JWT 발급 (AccessToken + RefreshToken)
        ↓
4. 이후 요청 시 Header에 Bearer 토큰 포함
        ↓
5. JwtAuthenticationFilter → 토큰 검증 → SecurityContext에 인증 정보 저장
```

---

## API 접근 권한

| 엔드포인트 | 권한 |
|---|---|
| POST /api/v1/auth/login | 누구나 |
| POST /api/v1/members | 누구나 (회원가입) |
| GET /api/v1/posts | 누구나 |
| GET /api/v1/posts/{id} | 누구나 |
| POST /api/v1/posts | 로그인 필요 |
| PATCH /api/v1/posts/{id} | 본인만 |
| DELETE /api/v1/posts/{id} | 본인만 |
| POST /api/v1/posts/{postId}/comments | 로그인 필요 |
| PATCH /api/v1/posts/{postId}/comments/{id} | 본인만 |
| DELETE /api/v1/posts/{postId}/comments/{id} | 본인만 |

---

## JWT 구조

### AccessToken
- 유효기간: 30분
- 용도: API 인증

### RefreshToken
- 유효기간: 7일
- 용도: AccessToken 재발급

---

## 구현 순서

- [x] 1. 의존성 추가
- [x] 2. `JwtTokenProvider` 구현
- [ ] 3. `JwtAuthenticationFilter` 구현
- [ ] 4. `CustomUserDetailsService` 구현
- [ ] 5. `SecurityConfig` 구현
- [ ] 6. `AuthController` (로그인/토큰 재발급) 구현
- [ ] 7. 기존 Controller에 인가 적용

---

## JwtAuthenticationFilter 상세 흐름

```
HTTP 요청
    ↓
JwtAuthenticationFilter.doFilterInternal()
    ↓
1. Header 추출
   request.getHeader("Authorization")
   → "Bearer eyJhbGci..." 형태
    ↓
2. 토큰 파싱
   token = header.substring(7)  // "Bearer " 제거
    ↓
3. 토큰 검증
   jwtTokenProvider.validateToken(token)
   → 유효하지 않으면 그냥 다음 필터로 넘김 (인증 없이 진행)
    ↓
4. memberId 추출
   jwtTokenProvider.getMemberId(token)
    ↓
5. UserDetails 조회
   customUserDetailsService.loadUserByUsername(memberId)
    ↓
6. SecurityContext에 인증 정보 저장
   UsernamePasswordAuthenticationToken authentication =
       new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
   SecurityContextHolder.getContext().setAuthentication(authentication)
    ↓
7. 다음 필터로 전달
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

| 메서드 | 역할 |
|---|---|
| `generateAccessToken(Long memberId)` | AccessToken 생성 (유효기간 30분) |
| `generateRefreshToken(Long memberId)` | RefreshToken 생성 (유효기간 7일) |
| `validateToken(String token)` | 토큰 서명/만료 검증 |
| `getMemberId(String token)` | 토큰에서 memberId 추출 |
| `getSigningKey()` | secretKey → SecretKey 변환 (내부용) |

---

## 변경사항

### Member 엔티티
- `password` 필드 추가
- `role` 필드 추가 (`ROLE_USER`, `ROLE_ADMIN`)

### MemberController
- `@RequestParam Long memberId` 제거 → JWT에서 꺼내서 사용
