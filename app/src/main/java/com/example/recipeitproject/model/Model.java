package com.example.recipeitproject.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance() {
        return _instance;
    }

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    private FirebaseModel firebaseModel = new FirebaseModel();
    private User loggedUser = null;

    private Model() {
        for (int i = 0; i < 20; i++) {
            addNewRecipe(new Recipe("name " + i, "" + i, "" + i, i));
        }

    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    List<Recipe> data = new LinkedList<>();
    List<String> category_list = new ArrayList(Arrays.asList("Salad", "Meat", "Desert", "Italian", "Asian"));

    public List<Recipe> getAllRecipes() {
        return data;
    }

    public List<String> getAllCategories() {
        return category_list;
    }

    public void addNewRecipe(Recipe rc) {
        data.add(rc);
    }

    public void fetchLoggedUser(Listener<Void> listener) {
        executor.execute(() -> {
            firebaseModel.fetchLoggedInUser(user -> {
                loggedUser = user;
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

}