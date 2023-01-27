package com.example.recipeitproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;

import java.util.List;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    TextView title_rh;
    ImageView image_src_rh;

    public RecipeViewHolder(@NonNull View itemView, RecipeRecyclerAdapter.OnItemClickListener listener) {
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




public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    OnItemClickListener listener;
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Recipe> data;

    public void setData(List<Recipe> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data){
        this.inflater = inflater;
        this.data = data;
    }

    void setOnItemClickLisetner(OnItemClickListener listener){
        this.listener=listener;
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.activity_recipe_detail_list, parent, false);
        return new RecipeViewHolder(view, listener);
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





