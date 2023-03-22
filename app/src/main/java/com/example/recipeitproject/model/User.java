package com.example.recipeitproject.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String id = "";
    private String username;
    private String email;
    private String password;
    public String imageUrl = "";
    public Long lastUpdated;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, String imageUrl) {
        this(username, email, password);
        this.imageUrl = imageUrl;
    }

    public User(String id, String username, String email, String password, String imageUrl) {
        this(username, email, password, imageUrl);
        this.id = id;
    }

    static final String COLLECTION = "users";
    static final String ID = "id";
    static final String USERNAME = "username";
    static final String EMAIL = "email";
    static final String PASSWORD = "password";
    static final String IMAGE_URL = "imageUrl";
    static final String LAST_UPDATED = "lastUpdated";

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static User fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String username = (String) json.get(USERNAME);
        String email = (String) json.get(EMAIL);
        String password = (String) json.get(PASSWORD);
        String imageUrl = (String) json.get(IMAGE_URL);
        User user = new User(id, username, email, password, imageUrl);
        try {
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            user.setLastUpdated(time.getSeconds());
        } catch (Exception e) {

        }

        return user;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(USERNAME, getUsername());
        json.put(EMAIL, getEmail());
        json.put(PASSWORD, getPassword());
        json.put(IMAGE_URL, getImageUrl());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }
}
