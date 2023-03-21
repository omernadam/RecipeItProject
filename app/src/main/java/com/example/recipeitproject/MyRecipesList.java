package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MyRecipesList extends AppCompatActivity {
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes_list);

        Intent intent = new Intent(this, AddRecipe_temp_activity.class);



        ImageButton addBtn = findViewById(R.id.btnAdd);








        addBtn.setOnClickListener(view -> {
            System.out.println("------");


        });

        bundle = new Bundle();
        bundle.putBoolean(RecipesViewerFragment.IS_IN_HOME_SCREEN, false);

        if (savedInstanceState == null) {
            RecipesViewerFragment fragment = new RecipesViewerFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_recipes_list, fragment)
                    .commit();
        }


    }
}