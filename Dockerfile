FROM openjdk:18-alpine
ARG JAR_FILE=target/test-post-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]