package com.example.recipeitproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.User;

public class MainScreenApp extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_app);

        bundle = new Bundle();
        bundle.putBoolean(FragmentRecipesViewer.IS_IN_HOME_SCREEN, true);

        Button profileBtn = findViewById(R.id.profile_btn);
        User user = Model.instance().getCurrentUser();
        String profileNickname = String.valueOf(user.getUsername().charAt(0));
        profileBtn.setText(profileNickname);

        if (savedInstanceState == null) {
            FragmentRecipesViewer fragment = new FragmentRecipesViewer();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_recipes_list, fragment)
                    .commit();
        }
    }
}