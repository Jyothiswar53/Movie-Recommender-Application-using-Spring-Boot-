package com.movie.movie_recommender.service;

import com.movie.movie_recommender.model.Movie;
import com.movie.movie_recommender.model.MovieDetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    @Value("${tmdb.api.key}")
    private String TMDB_API_KEY;

    @Value("${omdb.api.key}")
    private String OMDB_API_KEY;

    private final String TMDB_BASE = "https://api.themoviedb.org/3";
    private final RestTemplate restTemplate = new RestTemplate();

    public Movie searchMovieByQuery(String query) {
        String url = TMDB_BASE + "/search/movie?api_key=" + TMDB_API_KEY + "&query=" + query + "&include_adult=false";
        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            JsonNode firstResult = response.get("results").get(0);
            if (firstResult == null)
                return null;
            Movie movie = new Movie();
            movie.setId(firstResult.get("id").asInt());
            movie.setTitle(firstResult.get("title").asText());
            movie.setPosterPath(firstResult.get("poster_path").asText(null));
            movie.setReleaseDate(firstResult.get("release_date").asText(null));
            return movie;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Movie> fetchRecommendations(int movieId) {
        String url = TMDB_BASE + "/movie/" + movieId + "/recommendations?api_key=" + TMDB_API_KEY;
        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            List<Movie> recommendations = new ArrayList<>();
            for (JsonNode item : response.get("results")) {
                Movie movie = new Movie();
                movie.setId(item.get("id").asInt());
                movie.setTitle(item.get("title").asText());
                movie.setPosterPath(item.get("poster_path").asText(null));
                movie.setReleaseDate(item.get("release_date").asText(null));
                recommendations.add(movie);
            }
            return recommendations;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public MovieDetails fetchDetails(int movieId) {
        MovieDetails details = new MovieDetails();
        // Fetch TMDb details
        String detailsUrl = TMDB_BASE + "/movie/" + movieId + "?api_key=" + TMDB_API_KEY;
        String creditsUrl = TMDB_BASE + "/movie/" + movieId + "/credits?api_key=" + TMDB_API_KEY;
        String externalIdsUrl = TMDB_BASE + "/movie/" + movieId + "/external_ids?api_key=" + TMDB_API_KEY;

        try {
            JsonNode detailsNode = restTemplate.getForObject(detailsUrl, JsonNode.class);
            details.setId(detailsNode.get("id").asInt());
            details.setTitle(detailsNode.get("title").asText());
            details.setTagline(detailsNode.get("tagline").asText(null));
            details.setOverview(detailsNode.get("overview").asText(null));
            details.setPosterPath(detailsNode.get("poster_path").asText(null));
            details.setReleaseDate(detailsNode.get("release_date").asText(null));
            details.setOriginalLanguage(detailsNode.get("original_language").asText(null));
            details.setRuntime(detailsNode.get("runtime").asInt(0));
            details.setVoteAverage(detailsNode.get("vote_average").asDouble(0.0));
            details.setHomepage(detailsNode.get("homepage").asText(null));
            details.setGenres(detailsNode.get("genres").findValuesAsText("name"));

            // Fetch director
            JsonNode creditsNode = restTemplate.getForObject(creditsUrl, JsonNode.class);
            for (JsonNode crew : creditsNode.get("crew")) {
                if ("Director".equals(crew.get("job").asText())) {
                    details.setDirector(crew.get("name").asText());
                    break;
                }
            }

            JsonNode externalIds = restTemplate.getForObject(externalIdsUrl, JsonNode.class);
            details.setImdbId(externalIds.get("imdb_id").asText(null));

            if (OMDB_API_KEY != null && !OMDB_API_KEY.equals("YOUR_OMDB_API_KEY")) {
                String omdbUrl = "https://www.omdbapi.com/?apikey=" + OMDB_API_KEY + "&t=" + details.getTitle();
                if (details.getReleaseDate() != null) {
                    omdbUrl += "&y=" + details.getReleaseDate().substring(0, 4);
                }
                try {
                    JsonNode omdbNode = restTemplate.getForObject(omdbUrl, JsonNode.class);
                    if ("True".equals(omdbNode.get("Response").asText())) {
                        details.setOmdbPlot(omdbNode.get("Plot").asText(null));
                        details.setOmdbImdbRating(omdbNode.get("imdbRating").asText(null));
                        if (details.getImdbId() == null) {
                            details.setImdbId(omdbNode.get("imdbID").asText(null));
                        }
                        if (details.getDirector() == null) {
                            details.setDirector(omdbNode.get("Director").asText(null));
                        }
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            return null;
        }
        return details;
    }
}