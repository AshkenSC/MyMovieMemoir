package com.example.assignment3.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    private static final String TAG = "error";
    private String movieName;
    private String releaseDate;
    private String imageURL;

    public SearchResult(String movieName, String releaseDate, String imageURL) {
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.imageURL = imageURL;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public static List<SearchResult> createEntryList() {
        List<SearchResult> entries = new ArrayList<>();
        return  entries;
    }

}
