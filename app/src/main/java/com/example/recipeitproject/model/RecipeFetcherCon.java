package com.example.recipeitproject.model;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeFetcherCon {
    private static final String BASE_URL = "https://api.spoonacular.com/";
    private static final String API_KEY = "91f77cd54e1744a29f5725dcb075768a";

    public static void getRandomRecipe(int number, Callback<RecipeResponse> callback) {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpoonacularAPI api = retrofit.create(SpoonacularAPI.class);

        Call<RecipeResponse> call = api.getRandomRecipe(API_KEY, number);
        call.enqueue(callback);

    }
}
