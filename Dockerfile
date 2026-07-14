FROM ubuntu:24.04 AS build

RUN apt-get update && apt-get install -y openjdk-21-jdk maven
COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
EXPOSE 8082

COPY --from=build /target/courses_api_front-0.0.1.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]