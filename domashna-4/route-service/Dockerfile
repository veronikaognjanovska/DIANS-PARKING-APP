ARG POSTGRES_USER
ARG POSTGRES_PASSWORD
ARG POSTGRES_DB
ARG DB_SERVER

ENV spring.datasource.url jdbc:postgresql://${DB_SERVER}/${POSTGRES_DB}
ENV spring.datasource.username $POSTGRES_USER
ENV spring.datasource.password $POSTGRES_PASSWORD

FROM maven:3.6.0-jdk-11-slim AS build
COPY /route-service/src /route-service/src
COPY /route-service/pom.xml /route-service
RUN mvn -f /route-service/pom.xml clean package

FROM openjdk:14-jdk-alpine
ARG JAR_FILE=/route-service/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
