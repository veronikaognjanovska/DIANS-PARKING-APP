FROM openjdk:14-jdk-alpine
ARG JAR_FILE=/config-server/config-server/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]