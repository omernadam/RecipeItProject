package com.example.recipeitproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipeitproject.databinding.FragmentRecipesViewerBinding;
import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;

import java.util.List;
import java.util.stream.Collectors;

public class FragmentRecipesViewer extends Fragment {

    static final String IS_IN_HOME_SCREEN = "isInHomeScreen";
    final String ALL_CATEGORIES_FILTER = "All";

    FragmentRecipesViewerBinding binding;
    RecipeRecyclerAdapter adapter;
    Boolean isInHomeScreen = true;
    List<Recipe> recipesToShow;
    String userId = "";

    private List<Recipe> getRecipesToShow() {
        if (isInHomeScreen)
            return Model.instance().getAllRecipes();
        return Model.instance().getUserRecipes(userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecipesViewerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Spinner dropdown = view.findViewById(R.id.recipe_type_spinner);

        userId = Model.instance().getCurrentUser().getId();
        Model.instance().fetchCategories(
                idsByNames -> {
                    Model.instance().setCategoriesIdsByNames(idsByNames);
                },
                namesByIds -> {
                    Model.instance().setCategoriesNamesByIds(namesByIds);
                    createDropList(dropdown);
                }
        );

        if (getArguments() != null) {
            isInHomeScreen = getArguments().getBoolean(IS_IN_HOME_SCREEN);
        }

        Model.instance().fetchRecipes(recipes -> {
            Model.instance().setRecipes(recipes);
            recipesToShow = recipes;

            if (!isInHomeScreen) {
                recipesToShow = Model.instance().getUserRecipes(userId);
            }
        });

        binding.recycleList.setHasFixedSize(true);
        binding.recycleList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(), recipesToShow, isInHomeScreen);
        binding.recycleList.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Recipe rc = Model.instance().getAllRecipes().get(pos);
//                StudentsListFragmentDirections.ActionStudentsListFragmentToBlueFragment action = StudentsListFragmentDirections.actionStudentsListFragmentToBlueFragment(st.name);
//                Navigation.findNavController(view).navigate(action);
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                List<Recipe> recipesToShow = getRecipesToShow();
                String categoryName = (String) parent.getItemAtPosition(pos);

                if (!categoryName.equals(ALL_CATEGORIES_FILTER)) {
                    String categoryId = Model.instance().getCategoryIdByName(categoryName);
                    recipesToShow = recipesToShow.stream()
                            .filter(recipe -> categoryId.equals(recipe.getCategoryId()))
                            .collect(Collectors.toList());
                }
                adapter.setData(recipesToShow);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // viewModel = new ViewModelProvider(this).get(StudentsListFragmentViewModel.class);
    }

    void reloadData() {
//        binding.progressBar.setVisibility(View.VISIBLE);
        // Model.instance().refreshAllStudents();
    }

    public void createDropList(Spinner dropdown) {
        List<String> categoriesNames = Model.instance().getCategoriesNames();
        categoriesNames.add(0, ALL_CATEGORIES_FILTER);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoriesNames);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);
    }
}
