FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE

COPY target/${JAR_FILE} /app/comum-api.jar
COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh

EXPOSE 8080

CMD ["java", "-jar", "comum-api.jar"]
