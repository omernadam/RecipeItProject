package com.example.recipeitproject.model;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularAPI {
    @GET("recipes/random")
    Call<RecipeResponse> getRandomRecipe(
            @Query("apiKey") String apiKey,
            @Query("number") int number
    );
}
