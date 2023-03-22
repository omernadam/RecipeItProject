package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.User;

public class MainScreenApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_app);

        Button profile_button = findViewById(R.id.profile_btn);
        User user = Model.instance().getCurrentUser();
        String profileNickname = String.valueOf(user.getUsername().charAt(0));
        profile_button.setText(profileNickname);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainScreenApp.this, main_user_profile.class);
                startActivity(intent);
            }
        });

        Button fav_icon = findViewById(R.id.fav_icon);
        fav_icon.setOnClickListener((view -> {
            Intent intent = new Intent(MainScreenApp.this, RecipeFetcher.class);
            startActivity(intent);
            finish();
        }));

    }
}