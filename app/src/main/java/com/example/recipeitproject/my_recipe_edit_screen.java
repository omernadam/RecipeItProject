package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.recipeitproject.model.Category;
import com.example.recipeitproject.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class my_recipe_edit_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_edit_screen);

        createDropList();
    }

    private void createDropList() {
        Spinner dropdown = findViewById(R.id.my_recipe_edit_screen_recipe_type_spinner);
        List<String> categoriesNames = Model.instance().getCategoriesNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesNames);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);
    }
}