package com.example.recipeitproject.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance() {
        return _instance;
    }

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    private User loggedUser = null;
    private HashMap<String, User> usersByIds = new HashMap<>();
    private HashMap<String, String> categoryIdsByNames = new HashMap<>();
    private HashMap<String, String> categoryNamesByIds = new HashMap<>();
    private LiveData<List<Recipe>> recipes;
    private LiveData<List<Recipe>> userRecipes;

    private Model() {
    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    public enum LoadingState {
        LOADING,
        NOT_LOADING
    }

    final public MutableLiveData<LoadingState> EventRecipesListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);


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

    public LiveData<List<Recipe>> getAllRecipes() {
        if (recipes == null) {
            recipes = localDb.recipeDao().getAll();
            refreshAllRecipes();
        }

        return recipes;
    }

    public LiveData<List<Recipe>> getUserRecipes(String userId) {
        if (userRecipes == null) {
            userRecipes = localDb.recipeDao().getUserRecipes(userId);
        }
        return userRecipes;
    }

    public void refreshAllRecipes() {
        EventRecipesListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = Recipe.getLocalLastUpdate();
        // get all updated records from firebase since local last update
        firebaseModel.getAllRecipesSince(localLastUpdate, list -> {
            executor.execute(() -> {
                Log.d("TAG", "firebase return : " + list.size());
                Long time = localLastUpdate;
                for (Recipe recipe : list) {
                    // insert new records into ROOM
                    localDb.recipeDao().insertAll(recipe);
                    if (time < recipe.getLastUpdated()) {
                        time = recipe.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update local last update
                Recipe.setLocalLastUpdate(time);
                EventRecipesListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }

    public void addRecipe(Recipe recipe, Listener<Void> listener) {
        firebaseModel.addRecipe(recipe, (Void) -> {
            refreshAllRecipes();
            listener.onComplete(null);
        });
    }

    public void updateRecipe(Recipe recipe, Listener<Void> listener) {
        firebaseModel.updateRecipe(recipe, (Void) -> {
            refreshAllRecipes();
            listener.onComplete(null);
        });
    }

    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name, bitmap, listener);
    }

    public void logOut() {
        setCurrentUser(null);
        firebaseModel.logOutUser();
    }
}