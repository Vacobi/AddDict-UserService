FROM gradle:8.3-jdk20 AS build

WORKDIR /app

COPY . .

RUN gradle build -x test --no-daemon

FROM openjdk:20-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]