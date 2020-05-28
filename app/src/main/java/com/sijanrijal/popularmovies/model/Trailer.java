package com.sijanrijal.popularmovies.model;

import java.util.List;

public class Trailer {

    public List<TrailerLink> results;

    public static class TrailerLink {
        public String key;
        public String type;
    }
}
