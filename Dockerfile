# Start with a JDK base image
FROM openjdk:11-jdk-slim as builder

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw mvnw
COPY pom.xml .

# Pre-fetch dependencies (better layer caching)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# --- Production image ---
FROM openjdk:11-jdk-slim

# Set working directory
WORKDIR /app

# Copy the packaged JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port used by Spring Boot
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
