# Multistage dockerfile to take advantage of caching
FROM maven:3.6.3-jdk-11-slim as DEPS
WORKDIR /opt
COPY pom.xml /opt
RUN mvn dependency:go-offline


FROM maven:3.6.3-jdk-11-slim as COMPILER
WORKDIR /opt
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt /opt
COPY .  /opt/edition-ldod
WORKDIR /opt/edition-ldod
RUN mvn clean package -DskipTests=true -P war

FROM openjdk:18-alpine as BUILDER
COPY --from=compiler  /opt/edition-ldod/target/ldod.war ./ldod.war
RUN java -Djarmode=layertools -jar ldod.war extract

FROM openjdk:18-alpine
WORKDIR /opt/app
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.WarLauncher", "--server.port=8000"]
