# 베이스 이미지
FROM openjdk:17-jdk-alpine

# 작업 디렉토리
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY build/libs/admin-backoffice-0.0.1-SNAPSHOT.jar app.jar

# 업로드 폴더 복사 (컨테이너 안에서 접근 가능하도록)
COPY uploads /app/uploads

# 포트 노출
EXPOSE 8080

# 실행 명령
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app/app.jar"]
