package com.movie.movie_recommender.model;

import java.util.List;

public class MovieDetails {
    private int id;
    private String title;
    private String tagline;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private String originalLanguage;
    private Integer runtime;
    private Double voteAverage;
    private String imdbId;
    private String homepage;
    private List<String> genres;
    private String director;
    private String omdbPlot;
    private String omdbImdbRating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getOmdbPlot() {
        return omdbPlot;
    }

    public void setOmdbPlot(String omdbPlot) {
        this.omdbPlot = omdbPlot;
    }

    public String getOmdbImdbRating() {
        return omdbImdbRating;
    }

    public void setOmdbImdbRating(String omdbImdbRating) {
        this.omdbImdbRating = omdbImdbRating;
    }
}