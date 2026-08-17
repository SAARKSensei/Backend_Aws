# Start with a JDK base image
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy the pom.xml
COPY pom.xml .

# Copy the source code
COPY src ./src

# Package the application
RUN mvn -B clean package -DskipTests

# --- Production image ---
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy the packaged JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port used by Spring Boot
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
