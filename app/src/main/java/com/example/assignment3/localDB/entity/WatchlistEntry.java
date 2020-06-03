package com.example.assignment3.localDB.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WatchlistEntry {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    String imdbId;

    @ColumnInfo(name = "movie_name")
    String movieName;

    @ColumnInfo(name = "release_date")
    String releaseDate;

    @ColumnInfo(name = "add_date")
    String addDate;

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
