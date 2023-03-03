package com.example.main.ui;

public class Review {
    private String details,name,type;
    private int rating;

    public Review(String details, String name, String type, int rating) {
        this.details = details;
        this.name = name;
        this.type = type;
        this.rating = rating;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
