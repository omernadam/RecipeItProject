package com.example.recipeitproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipeitproject.databinding.FragmentRecipesViewerBinding;
import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;

import java.util.List;

public class RecipesViewerFragment extends Fragment {
    static final String IS_IN_HOME_SCREEN = "isInHomeScreen";
    final String ALL_CATEGORIES_FILTER = "All";

    FragmentRecipesViewerBinding binding;
    RecipeRecyclerAdapter adapter;
    RecipesViewerFragmentViewModel viewModel;
    Boolean isInHomeScreen = true;
    List<Recipe> recipesToShow;
    String categoryId = "";
    String userId = "";

    private LiveData<List<Recipe>> getRecipesToShow() {
        return isInHomeScreen
                ? (
                categoryId.equals("")
                        ? viewModel.getData()
                        : viewModel.getCategoryData(categoryId)
        )
                : (
                categoryId.equals("")
                        ? viewModel.getUserData(userId)
                        : viewModel.getCategoryUserData(userId, categoryId)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecipesViewerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Spinner dropdown = view.findViewById(R.id.recipe_type_spinner);

        userId = Model.instance().getCurrentUser().getId();
        if (getArguments() != null) {
            isInHomeScreen = getArguments().getBoolean(IS_IN_HOME_SCREEN);
        }

        if (isInHomeScreen) {
            Model.instance().fetchCategories(
                    idsByNames -> {
                        Model.instance().setCategoriesIdsByNames(idsByNames);
                    },
                    namesByIds -> {
                        Model.instance().setCategoriesNamesByIds(namesByIds);
                        namesByIds.keySet().forEach(id -> {
                            viewModel.getCategoryData(id).observe(getViewLifecycleOwner(), list -> {
                            });
                        });
                        createDropList(dropdown);
                    }
            );
        } else {
            createDropList(dropdown);
        }
        recipesToShow = getRecipesToShow().getValue();

        binding.recycleList.setHasFixedSize(true);
        binding.recycleList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(), recipesToShow, isInHomeScreen);
        binding.recycleList.setAdapter(adapter);

        Intent intent = new Intent(getContext(), UpdateRecipe_temp_activity.class);

        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Recipe recipe = recipesToShow.get(pos);
                intent.putExtra(RecipeFormFragment.RECIPE_TO_EDIT, (Parcelable) recipe);
                startActivity(intent);
//                StudentsListFragmentDirections.ActionStudentsListFragmentToBlueFragment action = StudentsListFragmentDirections.actionStudentsListFragmentToBlueFragment(st.name);
//                Navigation.findNavController(view).navigate(action);
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String categoryName = (String) parent.getItemAtPosition(pos);
                categoryId = !categoryName.equals(ALL_CATEGORIES_FILTER)
                        ? Model.instance().getCategoryIdByName(categoryName)
                        : "";

                recipesToShow = getRecipesToShow().getValue();
                adapter.setData(recipesToShow);
                binding.recycleList.scrollToPosition(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.progressBar.setVisibility(View.GONE);

        viewModel.getData().observe(getViewLifecycleOwner(), list -> {
            if (categoryId.equals("")) {
                adapter.setData(list);
            }
        });

        Model.instance().EventRecipesListLoadingState.observe(getViewLifecycleOwner(), status -> {
            binding.swipeRefresh.setRefreshing(status == Model.LoadingState.LOADING);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            reloadData();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipesViewerFragmentViewModel.class);
    }

    void reloadData() {
        Model.instance().refreshAllRecipes();
    }

    private void createDropList(Spinner dropdown) {
        List<String> categoriesNames = Model.instance().getCategoriesNames();
        categoriesNames.add(0, ALL_CATEGORIES_FILTER);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoriesNames);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);
    }
}
