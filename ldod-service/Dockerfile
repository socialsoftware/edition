# Multistage dockerfile to take advantage of caching
FROM maven:3.6.3-jdk-11-slim as DEPS

WORKDIR /opt/app

COPY search/pom.xml /opt/app/search/pom.xml
COPY visual/pom.xml /opt/app/visual/pom.xml
COPY front-end/pom.xml /opt/app/front-end/pom.xml

COPY repo /opt/app/repo

COPY pom.xml .
RUN mvn dependency:go-offline


FROM maven:3.6.3-jdk-11-slim as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app /opt/app


COPY search/src /opt/app/search/src
COPY visual/src /opt/app/visual/src
COPY front-end/src /opt/app/front-end/src


RUN mvn clean install -DskipTests=true


FROM openjdk:12-alpine
WORKDIR /opt/app/front-end
COPY --from=builder /opt/app/ /opt/app/
EXPOSE 8080
CMD [ "java", "-jar", "/opt/app/front-end/target/ldod.jar" ]