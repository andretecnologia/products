FROM openjdk:8-jdk-alpine
ARG PORT=8080
ARG JAR_FILE=target/api-0.0.1-SNAPSHOT.jar
VOLUME [ "/data" ]
EXPOSE ${PORT}
COPY ${JAR_FILE} /api/application/api.jar
WORKDIR /api/application
ENTRYPOINT [ "java", "-jar", "api.jar" ]