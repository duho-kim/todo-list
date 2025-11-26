# Todo List (Spring Boot + JSP)

간단한 Todo List 웹 애플리케이션입니다.  
Spring Boot + JSP로 구현되었으며, 로그인 없이도 사용할 수 있도록  
브라우저 쿠키로 사용자별 데이터를 구분합니다.

### 배포 URL  
https://todo-list-emob.onrender.com/tasks

---

## 주요 기능
- 할 일 생성 / 수정 / 삭제  
- 상태 변경 (TODO / DOING / DONE)  
- 제목 검색  
- 목표일 정렬  
- 로그인 없이 사용자별 데이터 자동 분리 (user_token)

---

## 기술 스택
- Java 17  
- Spring Boot 3  
- Spring MVC / JSP  
- Spring Data JPA  
- MySQL (Railway)  
- Docker / Render 배포

---

## 사용자 식별 방식 (중요 기능)
로그인 없이 사용하기 위해  
접속 시 브라우저에 `user_token` 쿠키를 발급합니다.

```java
Cookie cookie = new Cookie("user_token", UUID.randomUUID().toString());

모든 Task는 userToken 기준으로 조회·수정·삭제됩니다.

배포

Docker로 빌드하여 Render에 배포하고,
DB는 Railway MySQL을 사용합니다.

무료 플랜의 Sleep 문제는 UptimeRobot으로 해결했습니다.