FROM maven:latest AS build
COPY . /url-shortener-uaa
WORKDIR /url-shortener-uaa
RUN mvn clean package -DskipTests

FROM openjdk:latest
COPY --from=build /url-shortener-uaa/target/url-shortener-uaa-0.0.1-SNAPSHOT.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","application.jar"]