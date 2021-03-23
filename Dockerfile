# Multistage dockerfile to take advantage of caching
FROM maven:3.6.3-jdk-11-slim as DEPS

WORKDIR /opt/app
COPY text/pom.xml /opt/app/text/pom.xml
COPY user/pom.xml /opt/app/user/pom.xml
COPY virtual/pom.xml /opt/app/virtual/pom.xml
COPY notification/pom.xml /opt/app/notification/pom.xml
COPY search/pom.xml /opt/app/search/pom.xml
COPY recommendation/pom.xml /opt/app/recommendation/pom.xml
COPY visual/pom.xml /opt/app/visual/pom.xml
COPY game/pom.xml /opt/app/game/pom.xml
COPY front-end/pom.xml /opt/app/front-end/pom.xml

COPY pom.xml .
RUN mvn dependency:go-offline


FROM maven:3.6.3-jdk-11-slim as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app /opt/app

COPY text/src /opt/app/text/src
COPY user/src /opt/app/user/src
COPY virtual/src /opt/app/virtual/src
COPY notification/src /opt/app/notification/src
COPY search/src /opt/app/search/src
COPY recommendation/src /opt/app/recommendation/src
COPY visual/src /opt/app/visual/src
COPY game/src /opt/app/game/src
COPY front-end/src /opt/app/front-end/src


RUN mvn clean install -DskipTests=true


FROM openjdk:12-alpine
WORKDIR /opt/app/front-end
COPY --from=builder /opt/app/ /opt/app/
EXPOSE 8080
CMD [ "java", "-jar", "/opt/app/front-end/target/ldod.jar" ]