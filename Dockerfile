FROM eclipse-temurin:25-jre-alpine
LABEL authors="Sphelele"

# 3. Copy your JAR file into the container
# Replace the name below with your actual JAR name from the target folder
COPY target/nexus-grade-system-0.0.1-SNAPSHOT.jar app.jar

# 4. Set memory limits (crucial for your 1GB VM later)
ENV JAVA_OPTS="-Xmx512M -Xms256M"

# 5. Tell Docker the app runs on 8080
EXPOSE 8080

# 6. Command to run the app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]