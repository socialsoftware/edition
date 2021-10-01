# Multistage dockerfile to take advantage of caching
FROM maven:3.6.3-jdk-11-slim as DEPS

WORKDIR /opt/app
COPY classification-game/pom.xml /opt/app/classification-game/pom.xml
COPY edition-ldod/pom.xml /opt/app/edition-ldod/pom.xml
COPY ldod-visual/pom.xml /opt/app/ldod-visual/pom.xml


COPY pom.xml .
RUN mvn dependency:go-offline


FROM maven:3.6.3-jdk-11-slim as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app /opt/app

COPY classification-game  /opt/app/classification-game
COPY edition-ldod  /opt/app/edition-ldod
COPY ldod-visual  /opt/app/ldod-visual



RUN mvn clean install -DskipTests=true


FROM openjdk:12-alpine
WORKDIR /opt/app/edition-ldod
COPY --from=builder /opt/app/ /opt/app/
EXPOSE 8080
CMD [ "java", "-jar", "/opt/app/edition-ldod/target/ldod.jar" ]