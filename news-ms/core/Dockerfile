FROM openjdk:17

ARG JAR_FILE=build/libs/core-0.0.1.jar

COPY ${JAR_FILE} app.jar

WORKDIR /

ENTRYPOINT ["java", "-jar", "/app.jar"]
