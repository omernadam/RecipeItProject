package com.example.recipeitproject;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipeitproject.databinding.ActivityMyRecipesListBinding;

public class MyRecipesList extends Fragment {
    Bundle bundle;
    ActivityMyRecipesListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyRecipesListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnAdd.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_MyRecipesList_to_RecipeFormFragment);
        });

        bundle = new Bundle();
        bundle.putBoolean(RecipesViewerFragment.IS_IN_HOME_SCREEN, false);

        if (savedInstanceState == null) {
            RecipesViewerFragment fragment = new RecipesViewerFragment();
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .add(R.id.fragment_recipes_list, fragment)
                    .commit();
        }

        return view;
    }
}