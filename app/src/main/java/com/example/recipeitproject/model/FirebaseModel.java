package com.example.recipeitproject.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public class FirebaseModel {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    FirebaseModel() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private Task<QuerySnapshot> getUserByEmail(String email) {
        return db.collection(User.COLLECTION)
                .whereEqualTo(User.EMAIL, email)
                .get();
    }

    public void fetchLoggedInUser(Model.Listener<User> callback) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            getUserByEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            User user = null;
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                Map<String, Object> data = document.getData();
                                if (data != null) {
                                    user = User.fromJson(data);
                                }
                            }
                            callback.onComplete(user);
                        }
                    });
        }
    }

    public void createUser(User user, Model.Listener<Void> listener) {
        String email = user.getEmail();
        String password = user.getPassword();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.collection(User.COLLECTION).document(email).set(user.toJson())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d("TAG", "createUserWithEmail:success");
                                            listener.onComplete(null);
                                        }
                                    });
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public void loginUser(String email, String password, Model.Listener<Void> success, Model.Listener<Void> error) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getUserByEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            User user = null;
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                Map<String, Object> data = document.getData();
                                                if (data != null) {
                                                    user = User.fromJson(data);

                                                }
                                                Model.instance().setCurrentUser(user);
                                            }
                                            success.onComplete(null);
                                        }
                                    });
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            error.onComplete(null);
                        }
                    }
                });
    }
}
