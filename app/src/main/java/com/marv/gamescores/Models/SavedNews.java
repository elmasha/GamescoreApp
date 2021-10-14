package com.marv.gamescores.Models;

import java.util.Date;

public class SavedNews {
    private String  title,image,description,id,url,category;
    private Date timestamp;

    public SavedNews() {
    }

    public SavedNews(String title, String image, String description, String id, String url, String category, Date timestamp) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.id = id;
        this.url = url;
        this.category = category;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
