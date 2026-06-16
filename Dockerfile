# Use Eclipse Temurin 21 as base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file built by Maven
COPY target/menu-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port defined in application.yml
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
