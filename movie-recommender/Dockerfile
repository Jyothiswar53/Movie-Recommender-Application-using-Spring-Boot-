FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/movie-recommender-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=10000
EXPOSE ${PORT}

CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
