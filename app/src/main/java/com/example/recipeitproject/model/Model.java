package com.example.recipeitproject.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }

    private Model(){
        for(int i=0;i<20;i++){
            addNewRecipe(new Recipe("name " + i,""+i,""+i,i));
        }
    }

    List<Recipe> data = new LinkedList<>();
    List<String> category_list = new ArrayList<String>(Arrays.asList("Salad", "Meat", "Desert","Italian","Asian"));

    public List<Recipe> getAllRecipes(){
        return data;
    }
    public List<String> getAllCategories(){return category_list;}



    public void addNewRecipe(Recipe rc){
        data.add(rc);
    }




}