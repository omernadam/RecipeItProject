package com.example.recipeitproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipeitproject.databinding.FragmentRecipeFormBinding;
import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeFormFragment extends Fragment {

    public static String RECIPE_TO_EDIT = "recipeToEdit";

    FragmentRecipeFormBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Recipe recipe;
    String categoryName;
    Boolean isImageSelected = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.recipeImg.setImageBitmap(result);
                    isImageSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    binding.recipeImg.setImageURI(result);
                    isImageSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecipeFormBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Spinner categoriesDropdown = view.findViewById(R.id.categorySpinner);
        createDropList(categoriesDropdown);

        if (getArguments() != null) {
            recipe = getArguments().getParcelable(RECIPE_TO_EDIT);
            binding.titleEt.setText(recipe.getTitle());
            binding.descriptionEt.setText(recipe.getDescription());
            binding.categorySpinner.setSelection(Integer.parseInt(recipe.getCategoryId()) - 1);

            if (recipe.getImageUrl() != null && recipe.getImageUrl().length() > 5) {
                Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.noimage).into(binding.recipeImg);
            } else {
                binding.recipeImg.setImageResource(R.drawable.noimage);
            }
        }

        binding.saveBtn.setOnClickListener(view1 -> {
            String title = binding.titleEt.getText().toString();
            String description = binding.descriptionEt.getText().toString();

            if (title.length() > 0 && description.length() > 0) {
                String categoryId = Model.instance().getCategoryIdByName(categoryName);
                String userId = Model.instance().getCurrentUser().getId();
                String imageName = userId + '-' + title;
                Recipe recipe = new Recipe(title, categoryId, description, userId);

                if (isImageSelected) {
                    binding.recipeImg.setDrawingCacheEnabled(true);
                    binding.recipeImg.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.recipeImg.getDrawable()).getBitmap();

                    Model.instance().uploadImage(imageName, bitmap, url -> {
                        if (url != null) {
                            recipe.setImageUrl(url);
                        }
                        Model.instance().addRecipe(recipe, (unused) -> {
                            Log.d("TAG", "Recipe with image updated successfully");
//                        Navigation.findNavController(view1).popBackStack();
                            requireActivity().finish();
                        });
                    });
                } else {
                    Model.instance().addRecipe(recipe, (unused) -> {
                        Log.d("TAG", "Recipe without image updated successfully");
//                    Navigation.findNavController(view1).popBackStack();
                        requireActivity().finish();

                    });
                }
            } else {
                if (title.length() == 0) {
                    binding.titleEt.setError("Required");
                }
                if (description.length() == 0) {
                    binding.descriptionEt.setError("Required");
                }
            }
        });

        binding.cancelBtn.setOnClickListener(view1 -> {
//            Navigation.findNavController(view1).popBackStack(R.id.studentsListFragment,false));
            requireActivity().finish();

        });

        binding.cameraButton.setOnClickListener(view1 -> {
            cameraLauncher.launch(null);
        });

        binding.galleryButton.setOnClickListener(view1 -> {
            galleryLauncher.launch("image/*");
        });

        categoriesDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                categoryName = (String) parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    public void createDropList(Spinner dropdown) {
        List<String> categoriesNames = Model.instance().getCategoriesNames();
        categoryName = categoriesNames.get(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoriesNames);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);
    }
}
