FROM maven:3.6.0-jdk-11-slim AS build
COPY /user-service/user-service/src /user-service/src
COPY /user-service/user-service/pom.xml /user-service
RUN mvn -f /user-service/pom.xml clean package

ARG POSTGRES_USER
ARG POSTGRES_PASSWORD
ARG POSTGRES_DB
ARG DB_SERVER

ENV spring.datasource.url jdbc:postgresql://${DB_SERVER}/${POSTGRES_DB}
ENV spring.datasource.username $POSTGRES_USER
ENV spring.datasource.password $POSTGRES_PASSWORD

FROM openjdk:14-jdk-alpine
ARG JAR_FILE=/user-service/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
