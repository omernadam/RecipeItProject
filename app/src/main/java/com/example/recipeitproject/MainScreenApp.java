package com.example.recipeitproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

       RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter();
       list.setAdapter(adapter);



        adapter.setOnItemClickLisetner(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
//                intent_student.putExtra("pos", pos); //send position student data
//                startActivity(intent_student);

            }
        });
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView title_rh;
        ImageView image_src_rh;

        public RecipeViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            title_rh = itemView.findViewById(R.id.recipe_detail_list_title);
            image_src_rh=itemView.findViewById(R.id.recipe_detail_list_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);

                }
            });


        }
        public void bind(Recipe rc, int pos) {
            title_rh.setText(rc.title);
            image_src_rh.setImageResource(0);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
        OnItemClickListener listener;
        void setOnItemClickLisetner(OnItemClickListener listener){
            this.listener=listener;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.activity_recipe_detail_list, parent, false);
           return new RecipeViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            Recipe rc = data.get(position);
            holder.bind(rc, position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

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