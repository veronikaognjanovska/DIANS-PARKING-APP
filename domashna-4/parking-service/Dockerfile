FROM maven:3.6.0-jdk-11-slim AS build
COPY /parking-service/src /parking-service/src
COPY /parking-service/pom.xml /parking-service
RUN mvn -f /parking-service/pom.xml clean package

FROM openjdk:14-jdk-alpine
ARG JAR_FILE=/parking-service/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]