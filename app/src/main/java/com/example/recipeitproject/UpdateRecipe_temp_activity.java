package com.example.recipeitproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeitproject.model.Recipe;

public class UpdateRecipe_temp_activity extends AppCompatActivity {
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity_add_recipe);
        Recipe recipe = (Recipe) getIntent().getSerializableExtra(RecipeFormFragment.RECIPE_TO_EDIT);
        bundle = new Bundle();
        bundle.putParcelable(RecipeFormFragment.RECIPE_TO_EDIT, recipe);

        if (savedInstanceState == null) {
            RecipeFormFragment fragment = new RecipeFormFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.temp_add_recipe_fragment, fragment)
                    .commit();
        }
    }
}
