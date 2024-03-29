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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    private HashMap<String, LiveData<List<Recipe>>> recipesByCategoryId = new HashMap<>();
    private LiveData<List<Recipe>> userRecipes;
    private HashMap<String, LiveData<List<Recipe>>> userRecipesByCategoryId = new HashMap<>();

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
            usersByIds.put(user.getId(), user);
            setCurrentUser(user);
            listener.onComplete(null);
        });
    }

    public void updateUser(User user, Listener<Void> listener) {
        usersByIds.put(user.getId(), user);
        firebaseModel.updateUser(user, (Void) -> {
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

    public Boolean isUsernameNotExist(String username) {
        return usersByIds.values()
                .stream().noneMatch(user -> username.equals(user.getUsername()));
    }

    public String areUsernameOrEmailNotExist(String username, String email) {
        AtomicReference<String> existingDetail = new AtomicReference<>("");
        usersByIds.values().forEach(user -> {
                    if (username.equals(user.getUsername())) {
                        existingDetail.set("Username");
                    }
                    if (email.equals(user.getEmail())) {
                        existingDetail.set("Email");
                    }
                }
        );
        return existingDetail.get();
    }

    public String getCategoryIdByName(String name) {
        return categoryIdsByNames.get(name);
    }

    public String getCategoryNameById(String id) {
        return categoryNamesByIds.get(id);
    }

    public List<String> getCategoriesIds() {
        return new ArrayList<>(categoryNamesByIds.keySet());
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

    public LiveData<List<Recipe>> getCategoryRecipes(String categoryId) {
        if (!recipesByCategoryId.containsKey(categoryId)) {
            recipesByCategoryId.put(categoryId, localDb.recipeDao().getCategoryRecipes(categoryId));
        }
        return recipesByCategoryId.get(categoryId);
    }

    public LiveData<List<Recipe>> getUserRecipes(String userId) {
        if (userRecipes == null) {
            userRecipes = localDb.recipeDao().getUserRecipes(userId);
        }
        return userRecipes;
    }

    public LiveData<List<Recipe>> getCategoryUserRecipes(String userId, String categoryId) {
        if (!userRecipesByCategoryId.containsKey(categoryId)) {
            userRecipesByCategoryId.put(categoryId, localDb.recipeDao().getCategoryUserRecipes(userId, categoryId));
        }
        return userRecipesByCategoryId.get(categoryId);
    }

    public Boolean isUserRecipeNotExist(String title, String categoryId) {
        return userRecipes.getValue().stream().noneMatch(recipe ->
                title.equals(recipe.getTitle()) && categoryId.equals(recipe.getCategoryId())
        );
    }

    public Boolean isUserRecipeNotExist(String id, String title, String categoryId) {
        return userRecipes.getValue().stream().noneMatch(recipe ->
                !id.equals(recipe.getId()) &&
                        title.equals(recipe.getTitle()) && categoryId.equals(recipe.getCategoryId())
        );
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

    public void uploadImage(String name, Bitmap bitmap, Boolean isUserImage, Listener<String> listener) {
        firebaseModel.uploadImage(name, bitmap, isUserImage, listener);
    }

    public void logOut() {
        setCurrentUser(null);
        firebaseModel.logOutUser();
    }
}