# Stage 1: Build the application with Gradle
FROM gradle:latest as builder

WORKDIR /app

# Copy only the necessary files for dependency resolution
COPY build.gradle settings.gradle /app/
COPY src /app/src

# Resolve dependencies and build application
RUN gradle build -x test --no-daemon

# Stage 2: Run the application with a lightweight JRE
FROM openjdk:21

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/build/libs/*.jar /app/your-application.jar

# Expose the port if your application uses one
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "your-application.jar"]