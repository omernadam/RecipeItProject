package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.recipeitproject.model.Model;

public class main_user_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_profile);


        Button my_profile_button = findViewById(R.id.my_profile_btn);
        Button my_recipes_button = findViewById(R.id.my_recipes_btn);
        Button log_out_button = findViewById(R.id.log_out_btn);

        my_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(main_user_profile.this, myProfileScreen.class);
                startActivity(profileIntent);
            }
        });

        my_recipes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myRecipesIntent = new Intent(main_user_profile.this, MyRecipesList.class);
                startActivity(myRecipesIntent);

            }
        });

        log_out_button.setOnClickListener((view)->{
            logout();
        });

    }

    private void logout() {

        Model.instance().logOut();
        Intent intent = new Intent(this, LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}