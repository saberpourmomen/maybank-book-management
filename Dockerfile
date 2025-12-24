# ===== Build stage =====
FROM maven:3.9-eclipse-temurin-20 AS build
WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ===== Runtime stage =====
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8001

ENTRYPOINT ["java","-jar","app.jar"]
