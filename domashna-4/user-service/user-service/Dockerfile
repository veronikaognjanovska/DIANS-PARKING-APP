FROM maven:3.6.0-jdk-11-slim AS build
COPY /user-service/user-service/src /user-service/src
COPY /user-service/user-service/pom.xml /user-service
RUN mvn -f /user-service/pom.xml clean package

FROM openjdk:14-jdk-alpine
ARG JAR_FILE=/user-service/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]