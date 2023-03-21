package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MyRecipesList extends AppCompatActivity {
    Bundle bundle;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes_list);

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);

        Intent intent = new Intent(this, AddRecipe_temp_activity.class);
        ImageButton addBtn = findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(view -> {
//            startActivity(intent);
//            Navigation.findNavController(view).navigate(R.id.action_global_recipeFormFragment);
            navController.navigate(R.id.action_global_recipeFormFragment);
        });

        bundle = new Bundle();
        bundle.putBoolean(RecipesViewerFragment.IS_IN_HOME_SCREEN, false);

//        if (savedInstanceState == null) {
//            RecipesViewerFragment fragment = new RecipesViewerFragment();
//            fragment.setArguments(bundle);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_recipes_list, fragment)
//                    .commit();
//        }
    }
}