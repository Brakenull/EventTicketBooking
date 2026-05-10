# Giai đoạn 1: Build file JAR
FROM gradle:8-jdk21-alpine AS build
WORKDIR /app
COPY . .
# Chạy lệnh build của Gradle ngay trong Docker
RUN ./gradlew bootJar -x test

# Giai đoạn 2: Chạy ứng dụng
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Copy file jar từ giai đoạn build sang
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]