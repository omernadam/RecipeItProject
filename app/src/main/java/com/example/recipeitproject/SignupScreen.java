package com.example.recipeitproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.User;

public class SignupScreen extends AppCompatActivity {
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isImageSelected = false;
    ImageButton deletionButton;

    private void handleDeleteButtonAppearance(Boolean toShow) {
        deletionButton.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        Button signUpBtn = findViewById(R.id.signup_btn);
        Button loginBtn = findViewById(R.id.login_btn);
        ImageView userImg = findViewById(R.id.userImg);
        deletionButton = findViewById(R.id.deletionButton);
        ImageButton cameraButton = findViewById(R.id.cameraButton);
        ImageButton galleryButton = findViewById(R.id.galleryButton);
        TextView usernameEt = findViewById(R.id.username_signup_et);
        TextView emailEt = findViewById(R.id.email_signup_et);
        TextView passwordEt = findViewById(R.id.password_signup_et);
        handleDeleteButtonAppearance(false);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    userImg.setImageBitmap(result);
                    isImageSelected = true;
                    handleDeleteButtonAppearance(true);
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    userImg.setImageURI(result);
                    isImageSelected = true;
                    handleDeleteButtonAppearance(true);
                }
            }
        });

        Intent loginIntent = new Intent(this, LoginScreen.class);
        Intent mainScreenIntent = new Intent(this, MainScreenApp.class);

        deletionButton.setOnClickListener(view1 -> {
            userImg.setImageResource(R.drawable.noimage);
            isImageSelected = false;
            handleDeleteButtonAppearance(false);
        });

        cameraButton.setOnClickListener(view1 -> {
            cameraLauncher.launch(null);
        });

        galleryButton.setOnClickListener(view1 -> {
            galleryLauncher.launch("image/*");
        });

        signUpBtn.setOnClickListener(view -> {
            String username = usernameEt.getText().toString();
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            String userExistingDetail = Model.instance().areUsernameOrEmailNotExist(username, email);

            if (username.length() > 0 && email.length() > 0 && password.length() > 0 && userExistingDetail.equals("")
            ) {
                User user = new User(username, email, password);
                if (isImageSelected) {
                    userImg.setDrawingCacheEnabled(true);
                    userImg.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) userImg.getDrawable()).getBitmap();
                    String imageName = username + "-" + email;

                    Model.instance().uploadImage(imageName, bitmap, true, url -> {
                        if (url != null) {
                            user.setImageUrl(url);
                        }
                        Model.instance().createUser(user, (unused) -> {
                            startActivity(mainScreenIntent);
                            finish();
                        });
                    });
                } else {
                    Model.instance().createUser(user, (unused) -> {
                        startActivity(mainScreenIntent);
                        finish();
                    });
                }
            } else {
                if (username.length() == 0) {
                    usernameEt.setError("Required");
                }
                if (email.length() == 0) {
                    emailEt.setError("Required");
                }
                if (password.length() < 6) {
                    passwordEt.setError("Must have more than 5 characters");
                }
                if (!userExistingDetail.equals("")) {
                    Toast.makeText(this, userExistingDetail + " already exists",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn.setOnClickListener(view -> {
            startActivity(loginIntent);
            finish();
        });
    }
}