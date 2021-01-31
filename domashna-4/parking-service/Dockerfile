FROM maven:3.6.0-jdk-11-slim AS build
COPY /parking-service/src /parking-service/src
COPY /parking-service/pom.xml /parking-service
RUN mvn -f /parking-service/pom.xml clean package

ARG POSTGRES_USER
ARG POSTGRES_PASSWORD
ARG POSTGRES_DB
ARG DB_SERVER

ENV spring.datasource.url jdbc:postgresql://${DB_SERVER}/${POSTGRES_DB}
ENV spring.datasource.username $POSTGRES_USER
ENV spring.datasource.password $POSTGRES_PASSWORD

FROM openjdk:14-jdk-alpine
ARG JAR_FILE=/parking-service/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
