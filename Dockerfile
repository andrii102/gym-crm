FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/gym-app-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/logback.xml ./logback.xml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]