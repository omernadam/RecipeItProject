package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.User;


//includes the fragment of the list

public class MainScreenApp extends AppCompatActivity {
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_app);

        Button profile_button = findViewById(R.id.profile_btn);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainScreenApp.this, main_user_profile.class);
                startActivity(intent);
            }
        });

        ImageView fav_icon = findViewById(R.id.fav_icon);
        fav_icon.setOnClickListener((view -> {
            Intent intent = new Intent(MainScreenApp.this, RecipeFetcher.class);
            startActivity(intent);
            finish();
        }));


        bundle = new Bundle();
        bundle.putBoolean(RecipesViewerFragment.IS_IN_HOME_SCREEN, true);

        Button profileBtn = findViewById(R.id.profile_btn);
        User user = Model.instance().getCurrentUser();
        String profileNickname = String.valueOf(user.getUsername().charAt(0));
        profileBtn.setText(profileNickname);

        if (savedInstanceState == null) {
            RecipesViewerFragment fragment = new RecipesViewerFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_recipes_list, fragment)
                    .commit();
        }



    }

}