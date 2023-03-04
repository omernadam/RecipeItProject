package com.example.recipeitproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;

import java.util.List;

public class MainScreenApp extends AppCompatActivity {


    List<Recipe> data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_app);

        Button profile = findViewById(R.id.profile_btn);
        profile.setText("O");

        createDropList();


        data = Model.instance().getAllRecipes();


        RecyclerView list =findViewById(R.id.recycle_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));

       RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(getLayoutInflater(),data);
       list.setAdapter(adapter);




        adapter.setOnItemClickLisetner(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
//                intent_student.putExtra("pos", pos); //send position student data
//                startActivity(intent_student);

            }
        });
    }


    private void createDropList() {
        Spinner dropdown = findViewById(R.id.recipe_type_spinner);

        //Need to implement
        List<String> items = Model.instance().getAllCategories();
        items.add(0, "All");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);
    }


}