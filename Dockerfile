# Multi-stage Dockerfile: build with Maven, run on a JRE image.
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /workspace

# Copy only pom first to cache dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source and build jar
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime image (JRE)
FROM eclipse-temurin:21-jre

# Create non-root user (optional)
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /workspace/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Set Spring profile and optional JAVA_OPTS
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS=""

# Run the application
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE"]
