package com.example.assignment3.dataclass;

public class Memoir {
    private int memoirId;
    private String movieName;
    private String movieReleaseDate;
    private String watchDate;
    private String watchTime;
    private String comment;
    private int score;
    private Person personId;
    private Cinema cinemaId;
    private String cinemaPostcode;
    private String imdbId;

    public static class Cinema {
        int cinemaId;
        String cinemaName;
        String cinemaLocation;
        String cinemaPostcode;

        public Cinema(int cinemaId, String cinemaName, String cinemaPostcode) {
            this.cinemaId = cinemaId;
            this.cinemaName = cinemaName;
            this.cinemaLocation = "Australia";
            this.cinemaPostcode = cinemaPostcode;
        }

        public Cinema(int cinemaId, String cinemaName, String cinemaLocation, String cinemaPostcode) {
            this.cinemaId = cinemaId;
            this.cinemaName = cinemaName;
            this.cinemaLocation = cinemaLocation;
            this.cinemaPostcode = cinemaPostcode;
            this.cinemaName = "Cineplex";
        }
    }

    public Memoir(int memoirId, String movieName, String movieReleaseDate, String watchDate, String watchTime, String comment, int score, Person personId, Cinema cinemaId, String cinemaPostcode, String imdbId) {
        this.memoirId = memoirId;
        this.movieName = movieName;
        this.movieReleaseDate = movieReleaseDate;
        this.watchDate = watchDate;
        this.watchTime = watchTime;
        this.comment = comment;
        this.score = score;
        this.personId = personId;
        this.cinemaId = cinemaId;
        this.cinemaPostcode = cinemaPostcode;
        this.imdbId = imdbId;
    }


    public int getMemoirId() {
        return memoirId;
    }

    public void setMemoirId(int memoirId) {
        this.memoirId = memoirId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(String watchDate) {
        this.watchDate = watchDate;
    }

    public String getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(String watchTime) {
        this.watchTime = watchTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }

    public Cinema getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Cinema cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCinemaPostcode() {
        return cinemaPostcode;
    }

    public void setCinemaPostcode(String cinemaPostcode) {
        this.cinemaPostcode = cinemaPostcode;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
}
