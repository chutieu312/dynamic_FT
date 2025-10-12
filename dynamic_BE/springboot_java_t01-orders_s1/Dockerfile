# Multi-stage Dockerfile using official Gradle image to build and Temurin for runtime

# Build stage: use Gradle with JDK 21
FROM gradle:8.6-jdk21 AS build
WORKDIR /home/gradle/project
# Copy project files as the gradle user to leverage Gradle cache in the image
COPY --chown=gradle:gradle . .
# Build the bootJar (no daemon) - produces build/libs/*SNAPSHOT.jar
RUN gradle -q clean bootJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
EXPOSE 7011
# Copy jar produced by the build stage
COPY --from=build /home/gradle/project/build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]



