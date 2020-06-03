package com.example.assignment3.model;

public class WatchlistEntry {
    private String imdbId;
    private String movieName;
    private String releaseDate;
    private String addDate;

    public WatchlistEntry(String imdbId, String movieName, String releaseDate, String addDate) {
        this.imdbId = imdbId;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.addDate = addDate;
    }


    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }


}
