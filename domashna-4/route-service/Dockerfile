FROM maven:3.6.0-jdk-11-slim AS build
COPY /route-service/src /route-service/src
COPY /route-service/pom.xml /route-service
RUN mvn -f /route-service/pom.xml clean package

FROM openjdk:14-jdk-alpine
ARG JAR_FILE=/route-service/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]