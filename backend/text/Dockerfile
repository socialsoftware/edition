# Multistage dockerfile to take advantage of caching
FROM maven:3.6.3-jdk-11-slim as DEPS

WORKDIR /opt/app
COPY pom.xml /opt/app/pom.xml
COPY repo /opt/app/repo

RUN mvn dependency:go-offline


FROM maven:3.6.3-jdk-11-slim as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app /opt/app

COPY src /opt/app/src

RUN mvn clean install -DskipTests=true


FROM openjdk:12-alpine
WORKDIR /opt/app/
COPY --from=builder /opt/app/ /opt/app/
EXPOSE 8081
CMD [ "java", "-jar", "/opt/app/target/ldod.jar" ]