package com.dKrzmanc.coctailmixer.ui.alctypes;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.dKrzmanc.coctailmixer.MainActivity;
import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ui.recipes.CocktailListAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class AlcTypesFragment extends Fragment {

    private AlcTypesViewModel alcTypesViewModel;
    public RecyclerView.Adapter mAdapter;
    private SearchView searchViewAlcohol;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alcTypesViewModel = ViewModelProviders.of(this).get(AlcTypesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_alc_types, container, false);

        //Alcohol list
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.AlcoholListRecyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new AlcoholListAdapter(((MainActivity)getActivity()).AlcoholList);
        recyclerView.setAdapter(mAdapter);

        //Is not iconofied aka: can be clicked anywhere, not just search icon
        searchViewAlcohol = (SearchView)root.findViewById(R.id.AlcoholSearchView);
        searchViewAlcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewAlcohol.setIconified(false);
            }
        });


        //Set searchbar pref:
        searchViewAlcohol.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ((AlcoholListAdapter)mAdapter).filter(s);
                return false;
            }
        });

        //Show/Hide search bar, based on your prefs
        SharedPreferences mSharedPref = getActivity().getPreferences(MODE_PRIVATE);
        boolean bEnableSearch = mSharedPref.getBoolean("CocktailMixerbEnableSearch", false);
        ShowHideSearch(bEnableSearch, root);

        return root;
    }


    //If it doesnt get root -> Was called outside of onCreate()
    public void ShowHideSearch(Boolean i, View... root)
    {
        //Search Bar
        SearchView search;
        if (root.length == 0) {
            search = getView().findViewById(R.id.AlcoholSearchView);
        }
        else
        {
            search = root[0].findViewById(R.id.AlcoholSearchView);
        }

        //Get layout params
        ViewGroup.LayoutParams params = search.getLayoutParams();

        //Change height
        if (i)
        {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            params.height = 0;
        }
        search.setLayoutParams(params);


    }
}
