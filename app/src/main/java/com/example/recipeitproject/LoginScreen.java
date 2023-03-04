package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.User;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Button loginBtn = findViewById(R.id.login_btn);
        TextView signUpBtn = findViewById(R.id.signupnow_tv);
        TextView emailEt = findViewById(R.id.email_et);
        TextView passwordEt = findViewById(R.id.password_et);

        Intent signUpIntent = new Intent(this, SignupScreen.class);
        Intent mainScreenIntent = new Intent(this, MainScreenApp.class);


        User currentUser = Model.instance().getCurrentUser(unused -> {
            startActivity(mainScreenIntent);
        });
        System.out.println(currentUser);
        if (currentUser != null) {
            startActivity(mainScreenIntent);
        }

        loginBtn.setOnClickListener(view -> {
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();

            if (email.length() > 0 && password.length() > 0) {
                Model.instance().loginUser(email, password,
                        (success) -> {
                            startActivity(mainScreenIntent);
                        },
                        (error) -> {
                            Toast.makeText(this, "Invalid details",
                                    Toast.LENGTH_SHORT).show();
                        }
                );
            } else {
                if (email.length() == 0) {
                    emailEt.setError("Required");
                }
                if (password.length() == 0) {
                    passwordEt.setError("Required");
                }

            }
        });

        signUpBtn.setOnClickListener(view -> {
            startActivity(signUpIntent);
        });
    }
}