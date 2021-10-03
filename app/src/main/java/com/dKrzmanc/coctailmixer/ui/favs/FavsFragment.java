package com.dKrzmanc.coctailmixer.ui.favs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dKrzmanc.coctailmixer.MainActivity;
import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ui.recipes.CocktailListAdapter;
import com.dKrzmanc.coctailmixer.ui.recipes.CocktailListItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavsFragment extends Fragment {
    public RecyclerView.Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favs, container, false);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.CocktailMixerFavsRecycleView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<CocktailListItem> RefToFavCocktails = ((MainActivity)getActivity()).FavouriteCocktails;

        mAdapter = new CocktailListAdapter(RefToFavCocktails, this);
        recyclerView.setAdapter(mAdapter);

        return root;
    }

}
