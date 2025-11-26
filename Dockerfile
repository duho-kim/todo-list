# 1. Maven + JDK17 이미지로 WAR 빌드
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# 2. Tomcat 10 (JSP 지원) 기반 이미지로 WAR 실행
FROM tomcat:10.1-jdk17

# UTF-8 설정 (한글 깨짐 방지)
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

# 불필요한 기본 ROOT 앱 삭제
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# WAR 파일을 Tomcat ROOT 앱으로 배포
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Render는 $PORT 환경변수를 자동으로 넣어줌 ⇒ Tomcat에서도 사용하도록 설정
ENV SERVER_PORT=$PORT

EXPOSE 8080

CMD ["catalina.sh", "run"]
