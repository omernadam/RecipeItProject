package com.example.recipeitproject.model;

import java.util.HashMap;
import java.util.Map;

public class Recipe {

    private String id;
    private String title;
    private String categoryId;
    private String description;
    private String imageUrl;
    private String userId;

    static final String COLLECTION = "recipes";
    static final String ID = "id";
    static final String TITLE = "title";
    static final String CATEGORY_ID = "categoryId";
    static final String DESCRIPTION = "description";
    static final String IMAGE_URL = "imageUrl";
    static final String USER_ID = "userId";

    public Recipe(String id, String title, String categoryId, String description, String imageUrl, String userId) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return Model.instance().getCategoryNameById(categoryId);
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserId() { return userId; }

    public String getUsername() {
        return Model.instance().getUsernameById(userId);
    }

    public static Recipe fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String title = (String) json.get(TITLE);
        String categoryId = (String) json.get(CATEGORY_ID);
        String description = (String) json.get(DESCRIPTION);
        String image = (String) json.get(IMAGE_URL);
        String userId = (String) json.get(USER_ID);
        Recipe recipe = new Recipe(id, title, categoryId, description, image, userId);

        return recipe;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(TITLE, getTitle());
        json.put(CATEGORY_ID, getCategoryId());
        json.put(DESCRIPTION, getDescription());
        json.put(IMAGE_URL, getImageUrl());
        json.put(USER_ID, getUserId());

        return json;
    }

}
