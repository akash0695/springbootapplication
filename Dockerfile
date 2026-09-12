FROM eclipse-temurin:17-jre

WORKDIR /app

# Create a non-root user for improved security
RUN groupadd -r app && useradd -r -g app app || addgroup --system app && adduser --system --ingroup app app || true

# Copy the built Spring Boot fat JAR
COPY target/*.jar app.jar

# Ensure the app user owns the jar
RUN chown app:app app.jar || true

USER app

EXPOSE 8080

# Tunable JVM options (can be overridden at runtime)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
