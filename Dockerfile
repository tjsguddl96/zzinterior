# Stage 1: Build
FROM gradle:8.11.1-jdk17 AS build

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and dependencies
COPY gradlew gradlew
COPY gradle gradle

# Copy the source code
COPY . .

# Grant execution rights to Gradle wrapper
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build --no-daemon

# Stage 2: Run
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
