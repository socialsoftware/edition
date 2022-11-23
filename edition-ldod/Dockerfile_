FROM openjdk:8-alpine as builder
COPY target/ldod.jar ./ldod.jar
ENTRYPOINT ["java", "-jar", "ldod.jar", "--server.port=8000"]
