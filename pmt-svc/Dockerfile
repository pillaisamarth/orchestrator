# --- Stage 1: Build Stage ---
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# 1. Copy the maven wrapper and pom file first
# This allows Docker to cache your dependencies unless the pom.xml changes
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# 2. Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- Stage 2: Runtime Stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 3. Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# 4. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]