package com.example.recipeitproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.recipeitproject.model.RecipeApi;
import com.example.recipeitproject.model.RecipeFetcherCon;
import com.example.recipeitproject.model.RecipeResponse;

public class RecipeFetcher extends AppCompatActivity {

    private TextView recipeTitleTextView;
    private TextView recipeSummaryTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_fetcher);

        // Enable back button on the app bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recipeTitleTextView = findViewById(R.id.recipe_title_textview);
        recipeSummaryTextView = findViewById(R.id.recipe_summary_textview);

        RecipeFetcherCon.getRandomRecipe(4, new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    RecipeApi recipe = response.body().getRecipes().get(0);
                    String temp = recipe.getSummary();
                    String temp2 = stripHtmlTags(temp);
                    System.out.println(recipe.getTitle());
                    System.out.println(temp2);
                    recipeTitleTextView.setText(recipe.getTitle());
                    recipeSummaryTextView.setText(temp2);
                } else {
                    recipeTitleTextView.setText("Error");
                    recipeSummaryTextView.setText("Unable to retrieve recipe");
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                recipeTitleTextView.setText("Error");
                recipeSummaryTextView.setText("Unable to retrieve recipe");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainScreenApp.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String stripHtmlTags(String html) {
        return html.replaceAll("<[^>]*>", "");
    }
}
