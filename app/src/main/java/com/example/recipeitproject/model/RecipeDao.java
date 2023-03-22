package com.example.recipeitproject.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("select * from Recipe")
    LiveData<List<Recipe>> getAll();

    @Query("select * from Recipe where categoryId = :categoryId")
    LiveData<List<Recipe>> getCategoryRecipes(String categoryId);

    @Query("select * from Recipe where userId = :userId")
    LiveData<List<Recipe>> getUserRecipes(String userId);

    @Query("select * from Recipe where userId = :userId and categoryId = :categoryId")
    LiveData<List<Recipe>> getCategoryUserRecipes(String userId, String categoryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipes);
}
