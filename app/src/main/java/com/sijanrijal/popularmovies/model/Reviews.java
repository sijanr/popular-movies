package com.sijanrijal.popularmovies.model;

import java.util.List;

public class Reviews {

    public List<ReviewsContent> results;

    public static class ReviewsContent {
        public String author;
        public String content;
    }
}
