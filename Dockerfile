# Stage 1: Build với Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
# Stage 2: Chạy ứng dụng
FROM eclipse-temurin:21-jre
WORKDIR /app
# Lấy file jar từ stage build
COPY --from=build /app/target/*.jar app.jar
# Cổng 5050 theo application.yml
EXPOSE 5050
# Tối ưu RAM cho container
CMD ["java", "-Xms128m", "-Xmx384m", "-jar", "app.jar"]