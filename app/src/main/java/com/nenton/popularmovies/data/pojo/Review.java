package com.nenton.popularmovies.data.pojo;

/**
 * Created by serge on 06.04.2018.
 */

public class Review {
    private String author;
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
