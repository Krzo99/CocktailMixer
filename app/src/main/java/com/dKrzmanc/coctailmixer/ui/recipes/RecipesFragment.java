package com.dKrzmanc.coctailmixer.ui.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dKrzmanc.coctailmixer.CocktailListAdapter;
import com.dKrzmanc.coctailmixer.MainActivity;
import com.dKrzmanc.coctailmixer.R;

public class RecipesFragment extends Fragment {

    private RecipesViewModel recipesViewModel;
    public RecyclerView.Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recipesViewModel =
                ViewModelProviders.of(this).get(RecipesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);

        //Cocktail list
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.CocktailListrecyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new CocktailListAdapter(((MainActivity)getActivity()).CocktailList, this);
        recyclerView.setAdapter(mAdapter);

        return root;
    }
}