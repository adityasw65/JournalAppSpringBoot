# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy wrapper and pom
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Fix permissions for the maven wrapper
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 1. Create the system group and user
RUN addgroup --system spring && adduser --system spring --ingroup spring

# 2. Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# 3. FIX: Give the 'spring' user ownership of the /app directory
# This allows Logback to create 'journalApp.logs' without permission errors.
RUN chown -R spring:spring /app

# 4. Now switch to the non-root user
USER spring:spring

# Render dynamic port mapping
ENV SERVER_PORT=${PORT:-8080}
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]