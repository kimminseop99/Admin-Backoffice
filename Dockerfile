# 베이스 이미지
FROM openjdk:17-jdk-alpine

# 작업 디렉토리
WORKDIR /app

# JAR 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 정적 이미지 파일 복사 (resources/static/images)
COPY src/main/resources/static/images /app/static/images

# 업로드 디렉토리 복사
COPY uploads /app/uploads

# 포트 노출
EXPOSE 8080

# 실행
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]
