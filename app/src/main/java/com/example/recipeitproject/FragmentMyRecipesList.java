package com.example.recipeitproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.recipeitproject.databinding.FragmentMyRecipesListBinding;
import com.example.recipeitproject.model.Model;
import com.example.recipeitproject.model.Recipe;


public class FragmentMyRecipesList extends Fragment {

    FragmentMyRecipesListBinding binding;
    RecipeRecyclerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {

        binding = FragmentMyRecipesListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        View addButton = view.findViewById(R.id.btnAdd);
//        NavDirections action = StudentsListFragmentDirections.actionGlobalAddStudentFragment();
//        addButton.setOnClickListener(Navigation.createNavigateOnClickListener(action));
//        binding.progressBar.setVisibility(View.GONE);

//        viewModel.getData().observe(getViewLifecycleOwner(),list->{
//            adapter.setData(list);
//        });
//
//        Model.instance().EventStudentsListLoadingState.observe(getViewLifecycleOwner(),status->{
//            binding.swipeRefresh.setRefreshing(status == Model.LoadingState.LOADING);
//        });
//
//        binding.swipeRefresh.setOnRefreshListener(()->{
//            reloadData();
//        });
//
//        LiveData<List<Movie>> data = MovieModel.instance.searchMoviesByTitle("avatar");
//        data.observe(getViewLifecycleOwner(),list->{
//            list.forEach(item->{
//                Log.d("TAG","got movie: " + item.getTitle() + " " + item.getPoster());
//            });
//        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       // viewModel = new ViewModelProvider(this).get(StudentsListFragmentViewModel.class);
    }

    void reloadData(){
//        binding.progressBar.setVisibility(View.VISIBLE);
       // Model.instance().refreshAllStudents();
    }














    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMyRecipesList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_my_recipes_list.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMyRecipesList newInstance(String param1, String param2) {
        FragmentMyRecipesList fragment = new FragmentMyRecipesList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


}