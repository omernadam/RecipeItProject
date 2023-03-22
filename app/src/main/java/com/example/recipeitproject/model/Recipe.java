package com.example.recipeitproject.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.recipeitproject.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Recipe implements Parcelable, Serializable {

    @PrimaryKey
    @NonNull
    public String id = "";
    public String title = "";
    public String categoryId = "";
    public String description = "";
    public String imageUrl = "";
    public String userId = "";
    public Long lastUpdated;

    static final String COLLECTION = "recipes";
    static final String ID = "id";
    static final String TITLE = "title";
    static final String CATEGORY_ID = "categoryId";
    static final String DESCRIPTION = "description";
    static final String IMAGE_URL = "imageUrl";
    static final String USER_ID = "userId";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "recipes_local_last_update";

    @Ignore
    public Recipe(String title, String categoryId, String description, String userId) {
        this.title = title;
        this.categoryId = categoryId;
        this.description = description;
        this.userId = userId;
    }

    @Ignore
    public Recipe(String title, String categoryId, String description, String imageUrl, String userId) {
        this(title, categoryId, description, userId);
        this.imageUrl = imageUrl;
    }

    public Recipe(String id, String title, String categoryId, String description, String imageUrl, String userId) {
        this(title, categoryId, description, imageUrl, userId);
        this.id = id;
    }

    @Ignore
    protected Recipe(Parcel in) {
        id = in.readString();
        title = in.readString();
        categoryId = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        userId = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @NonNull
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

    public String getUserId() {
        return userId;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public String getUsername() {
        return Model.instance().getUsernameById(userId);
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static Recipe fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String title = (String) json.get(TITLE);
        String categoryId = (String) json.get(CATEGORY_ID);
        String description = (String) json.get(DESCRIPTION);
        String image = (String) json.get(IMAGE_URL);
        String userId = (String) json.get(USER_ID);
        Recipe recipe = new Recipe(id, title, categoryId, description, image, userId);
        try {
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            recipe.setLastUpdated(time.getSeconds());
        } catch (Exception e) {

        }

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
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());

        return json;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED, time);
        editor.commit();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(categoryId);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeString(userId);
    }
}
