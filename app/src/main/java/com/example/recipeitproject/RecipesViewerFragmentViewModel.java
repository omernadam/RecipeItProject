package com.example.recipeitproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;

import java.util.List;

public class RecipesViewerFragmentViewModel extends ViewModel {
    private LiveData<List<Recipe>> data = Model.instance().getAllRecipes();

    LiveData<List<Recipe>> getData() {
        return data;
    }

    LiveData<List<Recipe>> getCategoryData(String categoryId) {
        return Model.instance().getCategoryRecipes(categoryId);
    }

    LiveData<List<Recipe>> getUserData(String userId) {
        return Model.instance().getUserRecipes(userId);
    }

    LiveData<List<Recipe>> getCategoryUserData(String userId, String categoryId) {
        return Model.instance().getCategoryUserRecipes(userId, categoryId);
    }
}
