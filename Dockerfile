FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

RUN apk add --no-cache curl

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
            "-Dcom.sun.management.jmxremote", \
            "-Dcom.sun.management.jmxremote.port=9090", \
            "-Dcom.sun.management.jmxremote.rmi.port=9090", \
            "-Dcom.sun.management.jmxremote.authenticate=false", \
            "-Dcom.sun.management.jmxremote.ssl=false", \
            "-Djava.rmi.server.hostname=62.171.135.58", \
            "-jar", "app.jar"]