# 🌿 MomenTree, 순간을 함께하는 공간

### 🧑‍🤝‍🧑 다양한 관심사를 가진 사람들의 취미 공유 커뮤니티

이 프로젝트는 다양한 관심사를 가진 사용자들이 모여 취미를 공유하고 서로의 경험을 나눌 수 있는 커뮤니티 플랫폼입니다.
활동량에 따라 뱃지🏅와 순위🏆가 부여되며, 챌린지 참여를 통해 새로운 도전과 성취를 경험할 수 있습니다.
등급별로 차별화된 접근 권한을 제공하여 깊이 있는 소통과 유대감을 형성할 수 있도록 설계되었습니다.

<br>

## 🚀 1. 목표와 기능

### 1.1 목표

- 많은 사람들이 서로의 관심사를 공유하고 소통할 수 있는 공간 제공
- 커뮤니티를 통해 취미에 관해 소통하며 서로의 도움을 주고 받음
- 커뮤니티 활동을 통해 활동 점수를 얻고 순위에 따라 뱃지를 획득함으로써 성취감 획득
- 챌린지 게시판을 통해 다양한 취미를 시도하고 참여를 통해 수식어를 획득하여 참여를 유도

### 1.2 기능

* 사용방법
    1. 카카오 로그인을 해주세요
    2. 관심사에 따라 게시판에 들어가주세요
    3. 게시글을 작성하시거나 게시글에 댓글을 작성하실 수 있습니다
    4. 또한 챌린지 게시판에서 그에 해당하는 챌린지에 참여하실 수 있습니다

### 1.3 기존 커뮤니티와의 차별성

* 활동량 시스템을 통해 뱃지를 부여하여 활동을 유도
* 챌린지 게시판을 통해 다양한 취미를 시도하고, 참여를 통해 수식어를 획득하여 참여를 유도
* 특정 등급 이상부터 지역 게시판에 접근하여 지역별 취미를 공유하고 소통

<br>

## 🔗 2. 개발 환경 및 배포 URL

### 2.1 개발 환경

- **Web Framework**: `Spring Boot`
- **DB**: `AWS RDS` (MySQL)
- **Backend**: `Spring Boot` + `REST API`
- **Server**: `AWS EC2`
- **FrontEnd**: CSS + JavaScript + HTML
- **Auth**: `OAuth2`, `Spring Security`
- **IDE**: IntelliJ

### 2.2 아키텍처 설계도

<img src="https://github.com/user-attachments/assets/02ea1f51-ceba-46b9-8925-ae83c0bcd3a2" width="80%">

### 2.3 서비스 URL 정보

- 실행 URL: http://52.78.95.78:8080/
- `GitHub` 레포: https://github.com/ESTsoft-Back-end-6th-2nd-project-team3/ESTsoft-2nd-Project

<br>

## 📅 3. 프로젝트 관리와 개발 일정

### 3.1 팀 구성

- **팀장**: 황승현
- **팀원**: 언형민, 이슬기, 전지현

### 3.2 개발 기간

- **개발 기간**: 2024년 10월 28일 - 2024년 11월 13일
- **최종 수정 및 문서화**: 2024년 11월 12일 - 2024년 11월 13일

### 3.2 WBS

- [Notion 타임라인](https://oreumi.notion.site/bc0f904de9ed4905b44b47815fb61ad7?v=0fec363373844f20a44ff85d287a77ee)

<br>

## 📓 4. 요구사항 명세와 기능 명세

### 4.1 요구사항 명세

<img src="https://github.com/user-attachments/assets/6809ef18-76ba-4876-9748-64123670165c" width="80%">

<br>

## ️ 📁 5. 프로젝트 구조

### 5.1 프로젝트 구조

<details>
  <summary>📂 ESTsoft-2st-Project</summary>
┣ 📂 .gradle<br>
┣ 📂 .idea<br>
┣ 📂 build<br>
┣ 📂 gradle<br>
┣ 📂 src<br>
┃ ┣ 📂 main<br>
┃ ┃ ┣ 📂 java<br>
┃ ┃ ┃ ┣ 📂 com.estsoft.estsoft2ndproject<br>
┃ ┃ ┃ ┃ ┣ 📂 config<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 JasyptConfigAES<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 WebSecurityConfig<br>
┃ ┃ ┃ ┃ ┣ 📂 controller<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 api<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 AdminController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentApiController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ImageController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 MenuDataController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 MyPageController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ObjectiveController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 PostApiController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserController<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 main<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomErrorController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 PageController<br>
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 PostController<br>
┃ ┃ ┃ ┃ ┣ 📂 customException<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 AdditionalInformationRequireException<br>
┃ ┃ ┃ ┃ ┣ 📂 domain<br>
┃ ┃ ┃ ┃ ┃ ┣ 📂 dto<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 activityScore<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ScoreRequestDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 admin<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 PostListResponse<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UserLevelRequest<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserListResponse<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 comment<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentListResponseDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentRequestDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 CommentResponseDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 mypage<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ObjectiveRequestDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 PostResponseDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UserInfoRequestDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserInfoResponseDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 post<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentResponseDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 LikeRequestDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 PostRequestDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 PostResponseDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂 user<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomUserDetails<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 RegisterRequestDTO<br>
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserRequestDTO<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 ActivityScore<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 Category<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 Comment<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 Level<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 Likes<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 Objective<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 Post<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 PostType<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 Region<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 SubMenu<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 User<br>
┃ ┃ ┃ ┃ ┣ 📂 exception<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomAccessDeniedHandler<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 GlobalExceptionHandler<br>  
┃ ┃ ┃ ┃ ┃ ┣ 📜 PostNotFoundException<br>  
┃ ┃ ┃ ┃ ┃ ┗ 📜 UserNotFoundException<br> 
┃ ┃ ┃ ┃ ┣ 📂 repository<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 ActivityScoreRepository<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryRepository<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentRepository<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 LikesRepository<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 ObjectiveRepository<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 PostRepository<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 RegionRepository<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 UserRepository<br>
┃ ┃ ┃ ┃ ┣ 📂 service<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 AdminService<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentService<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 MenuDataService<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 MyPageService<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 ObjectiveService<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 PostService<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 UserService<br>
┃ ┃ ┃ ┃ ┗ 📜 EsTsoft2ndProjectApplication<br>
┃ ┃ ┣ 📂 resources<br>
┃ ┃ ┃ ┣ 📂 sql<br>
┃ ┃ ┃ ┃ ┗ 📜 Untitled.sql<br>
┃ ┃ ┃ ┣ 📂 static<br>
┃ ┃ ┃ ┃ ┣ 📂 css<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 base.css<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 reset.css<br>
┃ ┃ ┃ ┃ ┣ 📂 images<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 logo.png<br>
┃ ┃ ┃ ┃ ┣ 📂 img<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 kakao-icon.svg<br>
┃ ┃ ┃ ┃ ┣ 📂 js<br>
┃ ┃ ┃ ┃ ┗ ┗ 📜 bulletin-board-list.js<br>
┃ ┃ ┃ ┗ 📂 templates<br>
┃ ┃ ┃ ┃ ┣ 📂 fragment<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 admin-board-list.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 board-list.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 bulletin-board-list.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 category.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 category-best.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 category-name.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 edit-objective.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 edit-profile.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 main-page-best.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 main-page-signin.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 my-objective.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 mypage-profile.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 participated-challenge.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 register.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 search-all.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 view-comment.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 view-post.html<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 write-post.html<br>
┃ ┃ ┃ ┃ ┣ 📂 post<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 create-post.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 view-post.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 view-post-all.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 view-post-by-category.html<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 view-search-results.html<br>
┃ ┃ ┃ ┃ ┣ 📂 testHtml<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 comment-test.html<br>
┃ ┃ ┃ ┃ ┃ ┣ 📜 index.html<br>
┃ ┃ ┃ ┃ ┃ ┗ 📜 objectives_stats.html<br>
┃ ┃ ┃ ┗ 📜 application.yml<br>
┣ 📂 test<br>
┣ 📜 .gitignore<br>
┣ 📜 build.gradle<br>
┣ 📜 gradlew<br>
┣ 📜 README.md<br>
┗ 📜 settings.gradle<br>
</details>

<br>

### 5.2 데이터베이스 구조

- [DBDiagram](https://dbdiagram.io/d/6721d847b4216d5a28b12345)

<br>

### ERD

  <img src="https://github.com/user-attachments/assets/de305903-dfd0-40e6-b00b-4842088d0bd9" width="80%">

#### 📍 스키마 주요 사항

- **LIKES**
    - `like_type`: 게시글, 댓글 구분
    - `target_id`: 게시글, 댓글 id
- **POST**
    - `post_type`: 카테고리, 지역, 챌린지, 공지사항 구분
    - `category_id`: 카테고리, 지역 id

<br>

## 🖥️ 6. UI 구성 및 화면 정의서

### 6.1 메뉴 구조도

<img src="https://github.com/user-attachments/assets/95cd8bad-2603-4e04-9a54-e01d38152722" width="50%">

### 6.1 화면 정의서

| 화면 이름     | 화면 이미지                                                                                                  | 설명                                                                                                                                                                                                            |
|-----------|---------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 메인 화면     | <img src="https://github.com/user-attachments/assets/db13ef90-647c-4268-8663-48491e0e7ddd" width="50%"> | - 오늘의 베스트 게시글: 가장 인기 있는 게시글을 표시 <br> - 이달의 활동왕: 활동 점수가 높은 사용자 표시 <br> - 최근 게시글 목록: 최신 게시글을 빠르게 확인 <br> - 챌린지 참여 게시글 목록: 현재 진행 중인 챌린지 관련 게시글 표시                                                                |
| 챌린지 화면    | <img src="https://github.com/user-attachments/assets/5a427f69-bdae-4c6a-9ed4-fbd50bbb4be8" width="50%"> | - 카테고리 선택: 사용자가 특정 챌린지 카테고리를 선택할 수 있음 <br> - 오늘의 챌린지 베스트: 오늘 가장 인기 있는 챌린지 게시글 <br> - 이번 주 챌린지 베스트: 이번 주 동안 인기 있는 챌린지 게시글 <br> - 챌린지 게시글 목록: 선택한 챌린지에 대한 모든 게시글 표시 <br> - 챌린지 순위: 해당 챌린지에서 활동 점수가 높은 사용자 순위 표시 |
| 게시판 화면    | <img src="https://github.com/user-attachments/assets/145783d9-6d87-4eb1-b7ae-a4ab328b0319" width="50%"> | - 게시판에 해당하는 게시글 목록: 해당 게시판에 포함된 모든 게시글 표시 <br> - 게시판의 베스트 게시글: 인기 있는 게시글 순위로 표시 <br> - 게시판 하위 목록: 특정 주제나 카테고리의 세부 목록                                                                                          |
| 게시글 작성 화면 | <img src="https://github.com/user-attachments/assets/1df06c84-3a2a-4d27-a357-9c639359fe8c" width="50%"> | - 게시글 작성 폼: 사용자가 글 작성 시 텍스트와 이미지를 입력할 수 있는 폼                                                                                                                                                                  |
| 회원 가입 화면  | <img src="https://github.com/user-attachments/assets/eb396ac8-b926-4da5-964b-f814db9c634c" width="50%"> | - 사용자 회원가입: 새로운 사용자가 가입할 때 닉네임, 한줄 소개, SNS 링크를 입력할 수 있는 폼                                                                                                                                                     |
| 마이페이지 화면  | <img src="https://github.com/user-attachments/assets/800003bd-f215-42f5-b070-b158ab118d51" width="50%"> | - 목표를 확인하고 수정: 사용자가 자신의 목표를 설정하거나 수정 가능 <br> - 참여한 챌린지 확인: 사용자가 참여한 챌린지 목록을 확인                                                                                                                                |

<br>

## 🛠️ 7. 과업 및 기능 구현

### 7.1 주요 과업

- **알림 서비스**: 댓글, 공지사항 등의 알림 기능 제공
- **일반 로그인 및 회원가입**: OAuth2 외 일반 로그인ㆍ회원가입 기능 구현
- **등급 부여 시스템**: 활동 점수에 따른 등급 부여 구체화
- **이미지 삽입 기능**: 게시글 본문에 사진 삽입 기능 지원
- **닉네임 중복 방지**: 회원가입 시 닉네임 중복 검사
- **닉네임 변경 동시성 해결**: 다중 사용자 환경에서 닉네임 변경 시 발생할 수 있는 동시성 문제 해결
- **연속 클릭 방지**: 데이터 전송 시 연속 클릭 방지로 중복 전송 방지

<br>

## ⚠️ 8. 에러 및 트러블슈팅 히스토리

### 8.1 에러 사례 및 해결 과정

- **Spring Security 로그인 과정 중 Exception 처리 문제**
    - 발생 문제: 로그인 중 발생한 Exception이 `ControllerAdvice`에서 처리되지 않음
    - 해결 방법: Spring Security에서 발생하는 Exception은 `security filter chain`에서 처리해야
      하며, `.oauth2Login(oauth2 -> oauth2.failureHandler())`를 통해 Exception을 처리하도록 수정

- **Spring Security 리다이렉션 이후 CORS 에러**
    - 발생 문제: 리다이렉션 이후 카카오 로그인 진행 시 CORS 에러 발생
    - 해결 방법: 가입 페이지에서 서버로 `form` 데이터를 전송 후 응답을 받고 카카오 로그인 URL로 로그인 요청을 전송하여 해결

- **JavaScript에서 onclick 함수와 Thymeleaf 변수 사용 시 SyntaxError**
    - 발생 문제: `onclick` 함수 내에 Thymeleaf 변수를 사용 시 `SyntaxError` 발생
    - 해결 방법: `onclick`을 `th:onclick`으로 변경하고, `th:onclick="함수명(this.getAttribute('변수명'))"` 형식으로 함수를 호출하여 변수 할당을 적용


- **좋아요와 좋아요 취소 로직 통합 문제**
    - 발생 문제: 게시글 상세 페이지에서 좋아요 버튼을 눌러도 화면에 변경사항이 반영되지 않음
    - 원인 분석: toggleLike 메소드 하나로 좋아요와 좋아요 취소를 모두 처리하려다 보니, JavaScript의 렌더링 문제가 발생하여 좋아요 상태가 제대로 렌더링되지 않음
    - 해결 방법: 좋아요와 좋아요 취소 기능을 각각의 메소드 (like와 unlike)로 분리하여 각 상태에 따른 동작을 명확히 처리하도록 수정

- **좋아요 수 표시 문제**
    - 발생 문제: 좋아요 버튼을 누르면 글자 상태는 변경되지만, 좋아요 수가 화면에 업데이트되지 않음
    - 원인 분석: HTML의 span 태그에 타임리프(th:text)로 좋아요 수를 표시했으나, 초기값이 없을 경우 요소가 렌더링되지 않아 동적 업데이트가 불가능함
    - 해결 방법: 초기값이 없더라도 span 태그를 유지하여 좋아요 수가 0일 때도 빈 요소가 남아 동적으로 업데이트될 수 있도록 수정

- **좋아요 연속 클릭 시 에러 발생 문제**
    - 발생 문제: 연속해서 좋아요 버튼을 클릭할 경우, 좋아요 취소가 계속 누적되면서 서버에서 오류 발생
    - 원인 분석: 연속 클릭으로 인해 post의 like_count 값이 음수로 감소하고, 삭제할 like 데이터가 없어서 서버에서 500 오류 발생
      해결 방법:
    - 서버 측 조치: 컨트롤러에서 like 데이터가 존재하는지 확인한 후, 있을 때만 like 데이터 추가와 post count 증가를 처리
      클라이언트 측 조치: JavaScript에서 좋아요 버튼을 클릭할 때 버튼을 일시적으로 비활성화하고, 서버 응답이 완료된 후 버튼을 다시 활성화하는 로직을 추가하여 중복 클릭을 방지


- **Thymeleaf 조각 템플릿 렌더링 문제**
    - 발생 문제: Thymeleaf로 템플릿을 분리해 조각화할 때 th:replace 사용 시 특정 컴포넌트가 예상대로 렌더링되지 않는 문제가 발생
    - 해결 방법: 조각 템플릿에 필요한 변수를 명확하게 전달하고 th:replace 사용 방식을 재검토하여 각 조각이 올바르게 렌더링되도록 수정

<br>

## 📋 9. 참고 자료

### 9.1 참고 스타일 및 디자인

- **GitHub 블로그 스타일 참조**
    - [GitHub Engineering Blog](https://github.blog/category/engineering/)을 참고하여 UI/UX 디자인과 레이아웃에 반영

### 9.2 타임리프 템플릿 조각

- **Spring MVC - 타임리프(Thymeleaf) 템플릿 조각과 레이아웃**
    - `th:fragment`, `th:replace` 등을 사용하여 템플릿 구조를 구성하고 레이아웃을 관리

### 9.3 레퍼런스 이미지

| 참고 사이트                                                                                                   |
|----------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/46dbdffa-f4a6-4dc7-b574-e2f37317aa7d" width="100%"> |
