# App: Customer Registration
# Package: infrastructure
# File: Dockerfile
# Version: 0.1.0
# Turns: 1
# Author: AI
# Date: 2025-10-28T15:32:47Z
# Exports: Customer Registration container image
# Description: Builds a container image for the Customer Registration Spring Boot service using a multi-stage build.
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml
RUN ./mvnw -q dependency:go-offline
COPY src src
RUN ./mvnw -q package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/registration-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
