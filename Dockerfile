FROM openjdk:17
COPY target/*.jar app.jar
EXPOSE 18082
ENTRYPOINT ["java", "-jar", "/app.jar"]
