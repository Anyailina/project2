# Stage 1: Build the application with Gradle
FROM gradle:latest AS builder

WORKDIR /app

# Copy all necessary files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Copy the source code
COPY src /app/src

# Resolve dependencies and build the application
RUN ./gradlew build -x test --no-daemon

# Stage 2: Run the application with a lightweight JRE
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/build/libs/your-application.jar /app/your-application.jar

# Expose the port if your application uses one
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "your-application.jar"]