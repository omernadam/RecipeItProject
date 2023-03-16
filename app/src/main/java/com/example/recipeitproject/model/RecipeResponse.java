package com.example.recipeitproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeResponse {
    @SerializedName("recipes")
    private List<RecipeApi> recipes;

    public List<RecipeApi> getRecipes() {
        return recipes;
    }
}
