FROM openjdk:18-alpine as builder
COPY target/ldod.jar ./ldod.jar
RUN java -Djarmode=layertools -jar ldod.jar extract

FROM openjdk:18-alpine
WORKDIR /opt/app
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
EXPOSE 8000
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher", "--server.port=8000"]
