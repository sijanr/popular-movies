package com.sijanrijal.popularmovies.model;

import java.util.List;

public class Movie {
    public final int page;
    public final List<MovieInfo> results;

    public Movie(int page, List<MovieInfo> results) {
        this.page = page;
        this.results = results;
    }
}

