package com.example.recipeitproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;

import java.util.List;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView title_tv;
    ImageView image_src_iv;
    TextView category_tv;
    TextView description_tv;
    ImageButton edit_button;
    List<Recipe> data;


    public RecipeViewHolder(@NonNull View itemView, RecipeRecyclerAdapter.OnItemClickListener listener,List<Recipe> data, Boolean flag) {
        super(itemView);




        title_tv = itemView.findViewById(R.id.my_recipe_row_title_tv);
        image_src_iv = itemView.findViewById(R.id.my_recipe_row_image);
        category_tv = itemView.findViewById(R.id.my_recipe_row_category);
        description_tv = itemView.findViewById(R.id.my_recipe_row_description);
        edit_button = itemView.findViewById(R.id.my_recipe_row_edit_btn);

        if(flag)
            edit_button.setVisibility(View.INVISIBLE);

        this.data=data;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Recipe rc, int pos) {
        title_tv.setText(rc.title);
        image_src_iv.setImageResource(0);
        category_tv.setText(rc.category);
        description_tv.setText(rc.description);
    }
}

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    OnItemClickListener listener;
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Recipe> data;
    Boolean flag;

    public void setData(List<Recipe> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data, Boolean flag){
        this.inflater = inflater;
        this.data = data;
        this.flag = flag;
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_recipe_row, parent, false);
        return new RecipeViewHolder(view, listener,data, flag);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe rc = data.get(position);
        holder.bind(rc, position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }
}





