package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.recipeitproject.model.Model;

import java.util.List;

public class my_recipe_edit_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_edit_screen);

        createDropList();
    }

    private void createDropList() {
        Spinner dropdown = findViewById(R.id.my_recipe_edit_screen_recipe_type_spinner);

        //Need to implement
        List<String> items = Model.instance().getAllCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);
    }
}