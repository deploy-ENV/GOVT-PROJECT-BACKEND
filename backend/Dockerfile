# ---- Stage 1: Build ----
FROM maven:3.9.6-openjdk-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/target/govt_project-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
