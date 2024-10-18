#### Optimized ####
# Build stage
FROM openjdk:21-jdk-slim-bullseye AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix ./mvnw && \
    chmod +x ./mvnw && \
    ./mvnw package -DskipTests && \
    rm -rf /var/lib/apt/lists/*

# Runtime stage
FROM openjdk:21-jdk-slim-bullseye
WORKDIR /app
COPY --from=build /app/target/branchservice-0.0.1-SNAPSHOT.jar .
EXPOSE 8060

WORKDIR /usr/src/app

# Use Maven to build the application
COPY . .
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./mvnw
RUN chmod +x ./mvnw
RUN ./mvnw package -DskipTests

# Run the JAR file
ENTRYPOINT ["java","-jar","target/branchservice-0.0.1-SNAPSHOT.jar"]
