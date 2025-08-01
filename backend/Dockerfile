# Use OpenJDK image with JDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file (replace with your actual jar name)
COPY target/*.jar app.jar

# Expose port (Spring Boot default port)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
