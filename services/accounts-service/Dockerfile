#### Optimized ####
# Build stage
FROM openjdk:21-jdk-bullseye AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y \
    dos2unix \
    libfreetype6 \
    && apt-cache policy libfreetype6 \
    && rm -rf /var/lib/apt/lists/* \
    && dos2unix ./mvnw \
    && chmod +x ./mvnw \
    && ./mvnw package -DskipTests


# Runtime stage
FROM openjdk:21-jdk-bullseye
WORKDIR /app
COPY --from=build /app/target/accounts-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8090 9090
# Set JVM options for JMX
ENV JAVA_OPTS="-Dcom.sun.management.jmxremote \
               -Dcom.sun.management.jmxremote.port=9090 \
               -Dcom.sun.management.jmxremote.rmi.port=9090 \
               -Dcom.sun.management.jmxremote.authenticate=false \
               -Dcom.sun.management.jmxremote.ssl=false \
               -Djava.rmi.server.hostname=127.0.0.1 \
               -Xmx512m -Xms256m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar accounts-service-0.0.1-SNAPSHOT.jar"]