FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . /app
RUN mvn -f /app/race_application_query_service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/race_application_query_service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
