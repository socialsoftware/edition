FROM openjdk:18-alpine as builder
COPY target/ldod.war ./ldod.war
RUN java -Djarmode=layertools -jar ldod.war extract

FROM openjdk:18-alpine
WORKDIR /opt/app
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.WarLauncher", "--server.port=8000"]
