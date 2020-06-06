package com.example.assignment3.model;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class MemoirEntry {

    public String movieName;
    public String imdbId;
    public float imdbRating;
    public String watchDate;
    public String releaseDate;
    public String cinemaPostcode;
    public String comment;
    public int userRating;

    // load from Internet
    public String imageURL;

    public MemoirEntry(String movieName, String imdbId, String watchDate, String releaseDate,
                       String cinemaPostcode, String comment, int userRating) {
        this.movieName = movieName;
        this.imdbId = imdbId;
        this.imdbRating = imdbRating;
        this.watchDate = watchDate;
        this.releaseDate = releaseDate;
        this.cinemaPostcode = cinemaPostcode;
        this.comment = comment;
        this.userRating = userRating;
    }

    // copy constructor
    public MemoirEntry (MemoirEntry right) {
        this.movieName = right.movieName;
        this.imdbId = right.imdbId;
        this.imdbRating = right.imdbRating;
        this.watchDate = right.watchDate;
        this.releaseDate = right.releaseDate;
        this.cinemaPostcode = right.cinemaPostcode;
        this.comment = right.comment;
        this.userRating = right.userRating;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new MemoirEntry( this );
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

    public float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(float imdbRating) {
        this.imdbRating = imdbRating;
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
