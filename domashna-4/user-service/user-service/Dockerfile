
FROM openjdk:14-jdk-alpine
ARG JAR_FILE=/user-service/user-service/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
