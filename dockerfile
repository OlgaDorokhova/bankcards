FROM eclipse-temurin:21-jre
WORKDIR /app
COPY bankcards-0.0.1-SNAPSHOT.jar bankcards.jar
EXPOSE 8080
LABEL authors="fligh"

ENTRYPOINT ["java", "-jar", "bankcards.jar"]