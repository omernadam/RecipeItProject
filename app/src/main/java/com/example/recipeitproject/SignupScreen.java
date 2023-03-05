package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.User;

public class SignupScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        Button signUpBtn = findViewById(R.id.signup_btn);
        Button loginBtn = findViewById(R.id.login_btn);
        TextView usernameEt = findViewById(R.id.username_signup_et);
        TextView emailEt = findViewById(R.id.email_signup_et);
        TextView passwordEt = findViewById(R.id.password_signup_et);

        Intent loginIntent = new Intent(this, LoginScreen.class);
        Intent mainScreenIntent = new Intent(this, MainScreenApp.class);

        signUpBtn.setOnClickListener(view -> {
            String username = usernameEt.getText().toString();
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();

            if (username.length() > 0 && email.length() > 0 && password.length() > 0) {
                User user = new User(username, email, password);
                Model.instance().createUser(user, (unused) -> {
                    startActivity(mainScreenIntent);
                    finish();
                });
            } else {
                if (username.length() == 0) {
                    usernameEt.setError("Required");
                }
                if (email.length() == 0) {
                    emailEt.setError("Required");
                }
                if (password.length() == 0) {
                    passwordEt.setError("Required");
                }

            }
        });

        loginBtn.setOnClickListener(view -> {
            startActivity(loginIntent);
            finish();
        });
    }
}