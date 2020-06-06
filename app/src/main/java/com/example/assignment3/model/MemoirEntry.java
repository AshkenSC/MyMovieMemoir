package com.example.assignment3.model;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class MemoirEntry {

        String movieName;
        String imdbId;
        String watchDate;
        String releaseDate;
        String cinemaPostcode;
        String comment;
        int userRating;

        // load from Internet
        String imageURL;

        public MemoirEntry(String movieName, String imdbId, String watchDate, String releaseDate,
                           String cinemaPostcode, String comment, int userRating) {
            this.movieName = movieName;
            this.imdbId = imdbId;
            this.watchDate = watchDate;
            this.releaseDate = releaseDate;
            this.cinemaPostcode = cinemaPostcode;
            this.comment = comment;
            this.userRating = userRating;
        }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(String watchDate) {
        this.watchDate = watchDate;
    }

    public String getCinemaPostcode() {
        return cinemaPostcode;
    }

    public void setCinemaPostcode(String cinemaPostcode) {
        this.cinemaPostcode = cinemaPostcode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
