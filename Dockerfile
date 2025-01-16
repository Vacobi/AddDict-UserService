FROM gradle:8.3-jdk20 AS build

WORKDIR /app

COPY . .

RUN gradle build -x test --no-daemon

FROM openjdk:20-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://master_postgres:5432/note_db
ENV SPRING_DATASOURCE_USERNAME=user
ENV SPRING_DATASOURCE_PASSWORD=pswd

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]