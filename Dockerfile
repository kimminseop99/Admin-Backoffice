# --- Build Stage ---
FROM gradle:8.10.2-jdk21 AS build

WORKDIR /app
COPY . .

# Gradle 캐시 활용 (테스트 제외 빌드)
RUN ./gradlew clean build -x test

# --- Runtime Stage ---
FROM openjdk:21-jdk-slim

WORKDIR /app

# 빌드된 JAR만 복사
COPY --from=build /app/build/libs/admin-backoffice-0.0.1-SNAPSHOT.jar app.jar

# 업로드 폴더는 이미지에 넣지 않고 volume으로 관리
VOLUME /app/uploads

# 포트 노출
EXPOSE 8080

# 실행 명령 (Spring profile 적용)
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
