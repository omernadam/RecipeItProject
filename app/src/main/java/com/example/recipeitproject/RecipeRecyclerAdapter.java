package com.example.recipeitproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeitproject.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView titleTv;
    ImageView image;
    TextView usernameTv;
    TextView categoryTv;
    TextView descriptionTv;
    ImageButton editButton;
    List<Recipe> data;


    public RecipeViewHolder(@NonNull View itemView, RecipeRecyclerAdapter.OnItemClickListener listener, List<Recipe> data, Boolean isInHomeScreen) {
        super(itemView);
        titleTv = itemView.findViewById(R.id.my_recipe_row_title_tv);
        image = itemView.findViewById(R.id.my_recipe_row_image);
        usernameTv = itemView.findViewById(R.id.my_recipe_row_username);
        categoryTv = itemView.findViewById(R.id.my_recipe_row_category);
        descriptionTv = itemView.findViewById(R.id.my_recipe_row_description);
        editButton = itemView.findViewById(R.id.my_recipe_row_edit_btn);

        if (isInHomeScreen) {
            editButton.setVisibility(View.INVISIBLE);
        } else {
            usernameTv.setVisibility(View.INVISIBLE);
        }

        this.data = data;

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Recipe recipe, int pos) {
        titleTv.setText(recipe.getTitle());
        usernameTv.setText(recipe.getUsername());
        categoryTv.setText(recipe.getCategoryName());
        descriptionTv.setText(recipe.getDescription());
        if (recipe.getImageUrl() != null && recipe.getImageUrl().length() > 5) {
            Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.noimage).into(image);
        } else {
            image.setImageResource(R.drawable.noimage);
        }
    }
}

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Recipe> data;
    Boolean isInHomeScreen;

    public void setData(List<Recipe> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data, Boolean isInHomeScreen) {
        this.inflater = inflater;
        this.data = data;
        this.isInHomeScreen = isInHomeScreen;
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_recipe_row, parent, false);
        return new RecipeViewHolder(view, listener, data, isInHomeScreen);
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





