# 베이스 이미지
FROM openjdk:17-jdk-alpine

# 빌드된 jar 파일 경로
ARG JAR_FILE=build/libs/*.jar

# 앱 jar 복사
COPY ${JAR_FILE} app.jar

# 정적 이미지 파일 복사 (resources/static/images)
COPY src/main/resources/static/images /app/static/images

# 업로드 디렉토리 생성 (WebConfig와 일치)
RUN mkdir -p /temp/images

# 컨테이너에서 열 포트
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]
