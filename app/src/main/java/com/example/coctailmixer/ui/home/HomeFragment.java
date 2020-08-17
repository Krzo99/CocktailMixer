package com.example.coctailmixer.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coctailmixer.CocktailListAdapter;
import com.example.coctailmixer.MainActivity;
import com.example.coctailmixer.R;
import androidx.recyclerview.widget.DividerItemDecoration;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Cocktail list
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.CocktailListrecyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        RecyclerView.Adapter mAdapter = new CocktailListAdapter(((MainActivity)getActivity()).CocktailList, this);
        recyclerView.setAdapter(mAdapter);

        return root;
    }
}