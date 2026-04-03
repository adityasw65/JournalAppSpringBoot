# Stage 1: Build
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy wrapper and pom
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Dependencies
RUN ./mvnw dependency:go-offline

# Build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Security: Non-root user
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copy JAR
COPY --from=build /app/target/*.jar app.jar

# Render dynamic port mapping
ENV SERVER_PORT=${PORT:-8080}
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]