package com.example.recipeitproject.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance() {
        return _instance;
    }

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    private FirebaseModel firebaseModel = new FirebaseModel();
    private User loggedUser = null;
    private HashMap<String, User> usersByIds = new HashMap<>();
    private HashMap<String, String> categoryIdsByNames = new HashMap<>();
    private HashMap<String, String> categoryNamesByIds = new HashMap<>();
    List<Recipe> recipes = new LinkedList<>();

    private Model() {
    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    public void fetchLoggedUser(Listener<Void> listener) {
        executor.execute(() -> {
            firebaseModel.fetchLoggedInUser(user -> {
                setCurrentUser(user);
                if (user != null) {
                    listener.onComplete(null);
                }
            });
        });
    }

    public User getCurrentUser() {
        return loggedUser;
    }

    public void setCurrentUser(User user) {
        loggedUser = user;
    }

    public void createUser(User user, Listener<Void> listener) {
        firebaseModel.createUser(user, (Void) -> {
            setCurrentUser(user);
            listener.onComplete(null);
        });
    }

    public void loginUser(String email, String password, Listener<Void> onSuccess, Listener<Void> onError) {
        firebaseModel.loginUser(email, password,
                (Void) -> {
                    onSuccess.onComplete(null);
                },
                (Void) -> {
                    onError.onComplete(null);
                });
    }

    public void setUsersByIds(HashMap<String, User> hashMap) {
        usersByIds = hashMap;
    }

    public void fetchUsers(Listener<HashMap<String, User>> callback) {
        firebaseModel.getUsers(callback);
    }

    public String getUsernameById(String id) {
        return Objects.requireNonNull(usersByIds.get(id)).getUsername();
    }

    public String getCategoryIdByName(String name) {
        return categoryIdsByNames.get(name);
    }

    public String getCategoryNameById(String id) {
        return categoryNamesByIds.get(id);
    }

    public List<String> getCategoriesNames() {
        return new ArrayList<>(categoryNamesByIds.values());
    }

    public void setCategoriesIdsByNames(HashMap<String, String> hashMap) {
        categoryIdsByNames = hashMap;
    }

    public void setCategoriesNamesByIds(HashMap<String, String> hashMap) {
        categoryNamesByIds = hashMap;
    }

    public void fetchCategories(Listener<HashMap<String, String>> idsByNames, Listener<HashMap<String, String>> namesByIds) {
        firebaseModel.getCategories(idsByNames, namesByIds);
    }

    public List<Recipe> getAllRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> list) {
        recipes = list;
    }

    public void addRecipe(Recipe recipe, Listener<Void> listener) {
        recipes.add(recipe);
        firebaseModel.addRecipe(recipe, (Void) -> {
//            refresh
            listener.onComplete(null);
        });
    }

    public void fetchRecipes(Listener<List<Recipe>> callback) {
        firebaseModel.getRecipes(callback);
    }

    public List<Recipe> getUserRecipes(String userId) {
        return recipes.stream()
                .filter(recipe -> userId.equals(recipe.getUserId()))
                .collect(Collectors.toList());
    }

    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name, bitmap, listener);
    }

    public void logOut(){
        firebaseModel.logOutUser();
    }
}