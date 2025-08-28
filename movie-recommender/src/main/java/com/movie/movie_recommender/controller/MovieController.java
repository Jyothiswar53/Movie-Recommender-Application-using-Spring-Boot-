package com.movie.movie_recommender.controller;

import com.movie.movie_recommender.model.Movie;
import com.movie.movie_recommender.model.MovieDetails;
import com.movie.movie_recommender.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/search")
    public Movie searchMovie(@RequestParam String query) {
        return movieService.searchMovieByQuery(query);
    }

    @GetMapping("/recommendations/{movieId}")
    public List<Movie> getRecommendations(@PathVariable int movieId) {
        return movieService.fetchRecommendations(movieId);
    }

    @GetMapping("/details/{movieId}")
    public MovieDetails getDetails(@PathVariable int movieId) {
        return movieService.fetchDetails(movieId);
    }
}