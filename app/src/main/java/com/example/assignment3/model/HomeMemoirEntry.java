package com.example.assignment3.model;

import java.util.ArrayList;
import java.util.List;

public class HomeMemoirEntry {
    private String movieName;
    private String releaseDate;
    private double rating;

    public HomeMemoirEntry(String movieName, String releaseDate, double rating) {
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public static List<HomeMemoirEntry> createEntryList() {
        List<HomeMemoirEntry> entries = new ArrayList<>();
        return  entries;
    }
}
