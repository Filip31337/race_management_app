FROM maven:3.8.7-eclipse-temurin-17 as builder
WORKDIR /app
COPY . /app
RUN mvn -f /app/race-application-command-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/race-application-command-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
