package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.User;

public class myProfileScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_screen);

        //Display the current user details
        User user = Model.instance().getCurrentUser();

        EditText username_et = findViewById(R.id.username_my_profile_et);
        EditText email_et = findViewById(R.id.email_my_profile_et);
        EditText password_et = findViewById(R.id.my_profile_editTextPassword);

        username_et.setText(user.getUsername());
        email_et.setText(user.getEmail());
        password_et.setText(user.getPassword());


        Intent mainScreenIntent = new Intent(this, MainScreenApp.class);



        Button save_button = findViewById(R.id.my_profile_save_btn);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserName = username_et.getText().toString();
                String email = email_et.getText().toString();
                Model.instance().updateUserName(email, newUserName, new Model.Listener<Void>() {
                    @Override
                    public void onComplete(Void data) {
                        // Handle the update completion
                        Toast.makeText(myProfileScreen.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                        username_et.setText(newUserName);
                        startActivity(mainScreenIntent);
                        Model.instance().setCurrentUser(Model.instance().getCurrentUser());
                        System.out.println(Model.instance().getCurrentUser().getUsername());
                    }
                });
            }
        });

    }
}