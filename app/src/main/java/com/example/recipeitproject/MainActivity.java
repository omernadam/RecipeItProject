package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.recipeitproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}