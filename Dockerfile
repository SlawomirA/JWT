# Use Maven image with OpenJDK 21
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project descriptor files
COPY pom.xml ./
COPY .mvn .mvn

# Fetch Maven dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY src src

# Build the application
RUN mvn package

# Create a new stage for the runtime environment
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built application JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set environment variables
ARG PROJECT_NAME
ARG BUILD_DATE
ENV PROJECT_NAME=${PROJECT_NAME}
ENV BUILD_DATE=${BUILD_DATE}

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
