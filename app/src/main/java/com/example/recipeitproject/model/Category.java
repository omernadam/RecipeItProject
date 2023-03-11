package com.example.recipeitproject.model;

import java.util.Map;

public class Category {

    private String id;
    private String name;

    static final String COLLECTION = "categories";
    static final String ID = "id";
    static final String NAME = "name";

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Category fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String name = (String) json.get(NAME);
        Category category = new Category(id, name);

        return category;
    }
}
