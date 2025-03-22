FROM openjdk:21

COPY novel-0.0.1-SNAPSHOT.jar /web/novel/
EXPOSE 8080
WORKDIR /web/novel/
ENTRYPOINT ["java", "-jar", "novel-0.0.1-SNAPSHOT.jar"]