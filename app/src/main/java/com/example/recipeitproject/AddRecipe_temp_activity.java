package com.example.recipeitproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AddRecipe_temp_activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity_add_recipe);

        if (savedInstanceState == null) {
            RecipeFormFragment fragment = new RecipeFormFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.temp_add_recipe_fragment, fragment)
                    .commit();
        }
    }
}
