package com.example.recipeitproject.model;

import com.google.gson.annotations.SerializedName;

public class RecipeApi {
    @SerializedName("id")
    private int id;

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    @SerializedName("title")
    private String title;

    @SerializedName("summary")
    private String summary;

    @SerializedName("image")
    private String image;
}
