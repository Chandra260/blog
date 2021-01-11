package com.blog.models;

import org.springframework.stereotype.Component;

@Component
public class Filter {

    String author;
    String tags;
    String dateTime;
    String search;

    public Filter() {
        author = "";
        tags = "";
        dateTime = "";
        search = "";
    }

    @Override
    public String toString() {
        return "Filter{" +
                "author='" + author + '\'' +
                ", tags='" + tags + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", search='" + search + '\'' +
                '}';
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void initializeAuthor() {
        author = "";
    }

    public void initializeTags() {
        tags = "";
    }

    public void initializeSearch() {
        search = "";
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
