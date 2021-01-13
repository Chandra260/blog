package com.blog.models;

import org.springframework.stereotype.Component;

@Component
public class Filter {

    private String author;
    private String tags;
    private int dateTime;
    private String search;
    private boolean isAuthorFlag;
    private boolean isTagsFlag;

    @Override
    public String toString() {
        return "Filter{" +
                "author='" + author + '\'' +
                ", tags='" + tags + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", search='" + search + '\'' +
                ", isAuthorFlag=" + isAuthorFlag +
                ", isTagsFlag=" + isTagsFlag +
                '}';
    }

    public boolean isAuthorFlag() {
        return isAuthorFlag;
    }

    public void setAuthorFlag(boolean authorFlag) {
        isAuthorFlag = authorFlag;
    }

    public boolean isTagsFlag() {
        return isTagsFlag;
    }

    public void setTagsFlag(boolean tagsFlag) {
        isTagsFlag = tagsFlag;
    }

    public Filter() {
        author = "";
        tags = "";
        dateTime = 365*5;
        search = "";
        isAuthorFlag = true;
        isTagsFlag = true;
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

    public int getDateTime() {
        return dateTime;
    }

    public void setDateTime(int dateTime) {
        this.dateTime = dateTime;
    }
}
