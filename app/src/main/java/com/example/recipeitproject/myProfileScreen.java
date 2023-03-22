package com.example.recipeitproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.User;
import com.squareup.picasso.Picasso;

public class myProfileScreen extends AppCompatActivity {
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
        setContentView(R.layout.activity_my_profile_screen);

        //Display the current user details
        User user = Model.instance().getCurrentUser();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView userImg = findViewById(R.id.userImg);
        deletionButton = findViewById(R.id.deletionButton);
        ImageButton cameraButton = findViewById(R.id.cameraButton);
        ImageButton galleryButton = findViewById(R.id.galleryButton);
        EditText username_et = findViewById(R.id.username_my_profile_et);

        Button save_button = findViewById(R.id.my_profile_save_btn);

        if(user.getImageUrl().equals("")) {
            handleDeleteButtonAppearance(false);
        } else {
            Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.noimage).into(userImg);
            isImageSelected = true;
        }

        username_et.setText(user.getUsername());


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

        save_button.setOnClickListener(view1 -> {
            String username = username_et.getText().toString();
            Boolean isUsernameExist = Model.instance().isUsernameNotExist(username);
            if (username.length() > 0 && isUsernameExist) {
                user.setUsername(username);
                if (isImageSelected) {
                    userImg.setDrawingCacheEnabled(true);
                    userImg.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) userImg.getDrawable()).getBitmap();
                    String imageName = username + "-" + user.getEmail();

                    Model.instance().uploadImage(imageName, bitmap, true, url -> {
                        if (url != null) {
                            user.setImageUrl(url);
                        }
                        Model.instance().updateUser(user, (unused) -> {
                            finish();
                        });
                    });
                } else {
                    user.setImageUrl("");
                    Model.instance().updateUser(user, (unused) -> {
                        finish();
                    });
                }
            } else {
                if (username.length() == 0) {
                    username_et.setError("Required");
                }
                else {
                    Toast.makeText(this, "Username already exists",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}