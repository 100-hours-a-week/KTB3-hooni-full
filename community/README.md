# Tennis-Mate
> 테니스 매칭을 위한 1인 웹 커뮤니티 서비스입니다.
>
>실력/지역/시간대가 맞는 테니스 친구를 찾고, 매칭을 등록·검색해보세요~

# 기술 스택
### Java21 / Spring Boot 3.5.6

### Spring Security 3.5.6

### Jpa 3.1.0

### MySQL 9.4.0

# 폴더 구조
```text
root
├─ domain
│  ├─ auth
│  ├─ comment
│  ├─ common
│  ├─ image
│  ├─ post
│  ├─ postlike
│  └─ user
├─ global
│  ├─ apidoc
│  ├─ auth
│  ├─ config
│  ├─ entity
│  ├─ exception
│  ├─ page
│  └─ security
├─ infrastructure
   ├─ image
   ├─ jpa
   └─ token
```

# api 문서

| 도메인               | 기능이름 | HTTP 메서드 | URL |
|-------------------|---------|------------|-----|
| 인증(Auth)          | 로그인 | POST | /auth/login |
|                   | 토큰 재발급 | POST | /auth/reissue |
| 사용자(User)         | 회원가입 | POST | /users |
|                   | 프로필 정보 변경 | PATCH | /users/me |
|                   | 비밀번호 변경 | PATCH | /users/me/change-password |
|                   | 계정 삭제  | DELETE | /users/me |
|                   | 프로필 조회 | GET | /users/me |
| 게시글(Post)         | 게시글 생성 | POST | /posts |
|                   | 게시글 목록 조회 | GET | /posts?page={page} |
|                   | 게시글 상세 조회 | GET | /posts/{postId} |
|                   | 게시글 수정 | PUT | /posts/{postId} |
|                   | 게시글 삭제 | DELETE | /posts/{postId} |
| 게시글 좋아요(PostLike) | 게시글 좋아요 | PUT | /posts/{postId}/like |
|                   | 게시글 좋아요 취소 | DELETE | /posts/{postId}/like |
| 댓글(Comment)       | 댓글 생성 | POST | /posts/{postId}/comments |
|                   | 댓글 수정    | PATCH | /posts/{postId}/comments/{commentId} |
|                   | 댓글 삭제    | DELETE | /posts/{postId}/comments/{commentId} |
|                   | 댓글 조회    | GET | /posts/{postId}/comments?cursor={cursor} |
| 이미지(Image)        | 이미지 조회 | GET | /images/{id} |

# ERD
<img width="400" height="580" src="https://github.com/user-attachments/assets/ece90424-df50-4ee2-b490-a92fca4ca58a">


# 영상
https://github.com/user-attachments/assets/1e77bf24-0b60-4d6d-8765-a93f97c0968f



# 기여자 정보
| 역할    | 이름 | GitHub                              |
|-------|----|-------------------------------------|
| 1인 개발 | 정용훈 | [링크](https://github.com/hoooonshub) |
