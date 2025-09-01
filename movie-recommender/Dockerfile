FROM openjdk:17-jdk-alpine

WORKDIR /movie-recommender

COPY target/movie-recommender-0.0.1-SNAPSHOT.jar movie-recommender.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "movie-recommender.jar"]