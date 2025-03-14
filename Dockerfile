FROM eclipse-temurin:21

WORKDIR /app

COPY target/equipment-rental-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
