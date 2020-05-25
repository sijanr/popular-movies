package com.sijanrijal.popularmovies.model;

import java.util.List;

public class Genre {
    public List<Attributes> genres;

    public static class Attributes {
        public int id;
        public String name;
    }

}
