package com.anf.core.components.models;

//import com.day.cq.dam.api.Asset;

public class NewsItem {
    String title;
    //String link;
    String date;
    String author;
    String image;
    //String imageSrc;
    String description;
    //Asset imageAsset;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

 /*   public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }*/
}
