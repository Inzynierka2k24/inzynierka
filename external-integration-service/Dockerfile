# Build stage
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY settings.xml /root/.m2/settings.xml
RUN mvn -f /usr/src/app/pom.xml clean package

# Package stage
FROM eclipse-temurin:17-alpine
COPY --from=build /usr/src/app/target/*.jar /usr/app/service.jar
EXPOSE 8080 6565
ENTRYPOINT ["java","-jar","/usr/app/service.jar"]