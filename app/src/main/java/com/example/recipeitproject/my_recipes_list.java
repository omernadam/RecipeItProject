package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;

import java.util.List;

public class my_recipes_list extends AppCompatActivity {

    List<Recipe> data;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes_list);
        bundle = new Bundle();
        bundle.putBoolean("flag", false);

        data = Model.instance().getAllRecipes();


        if (savedInstanceState == null) {
            FragmentMyRecipesList fragment = new FragmentMyRecipesList();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_recipes_list, fragment)
                    .commit();
        }


    }
}