package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MyRecipesList extends AppCompatActivity {
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes_list);
        bundle = new Bundle();
        bundle.putBoolean(FragmentRecipesViewer.IS_IN_HOME_SCREEN, false);

        if (savedInstanceState == null) {
            FragmentRecipesViewer fragment = new FragmentRecipesViewer();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_recipes_list, fragment)
                    .commit();
        }
    }
}