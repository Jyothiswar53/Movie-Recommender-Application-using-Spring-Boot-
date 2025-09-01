FROM eclipse-temurin:17-jdk

WORKDIR /movie-recommender

COPY . .

RUN ./mvnw package -DskipTests

CMD ["java", "-jar", "target/movie-recommender-0.0.1-SNAPSHOT.jar"]
