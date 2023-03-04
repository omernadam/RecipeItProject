package com.example.recipeitproject.model;

public class Recipe {

    public String title;
    public String category;
    public String description;
    public Integer image_src;

    public Recipe(String title, String category, String description, Integer image_src) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.image_src = image_src;
    }
}
