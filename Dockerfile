# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy the gradle/maven executable and wrapper
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (this layer is cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline

# Copy source code and build the jar
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create a non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copy only the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render uses the PORT environment variable; Spring needs to listen to it
ENV SERVER_PORT=${SERVER_PORT}
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
