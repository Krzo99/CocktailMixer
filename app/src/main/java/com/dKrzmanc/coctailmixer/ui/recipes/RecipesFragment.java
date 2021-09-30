package com.dKrzmanc.coctailmixer.ui.recipes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dKrzmanc.coctailmixer.MainActivity;
import com.dKrzmanc.coctailmixer.R;

import static android.content.Context.MODE_PRIVATE;

public class RecipesFragment extends Fragment {

    private RecipesViewModel recipesViewModel;
    public RecyclerView.Adapter mAdapter;

    private SearchView searchViewCocktails;

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

        searchViewCocktails = (SearchView)root.findViewById(R.id.searchCocktails);
        searchViewCocktails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewCocktails.setIconified(false);
            }
        });
        searchViewCocktails.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ((CocktailListAdapter)mAdapter).filter(s);
                return false;
            }
        });


        //Show/Hide search bar, based on your prefs
        SharedPreferences mSharedPref = getActivity().getPreferences(MODE_PRIVATE);
        boolean bEnableSearch = mSharedPref.getBoolean("CocktailMixerbEnableSearch", false);
        ShowHideSearch(bEnableSearch, true, root);

        mAdapter = new CocktailListAdapter(((MainActivity)getActivity()).CocktailList, this);
        recyclerView.setAdapter(mAdapter);

        return root;
    }


    //If it doesnt get root -> Was called outside of onCreate()
    //bShouldUpdateQuery -> So we dont change queries, if layout is not fully created yet!
    public void ShowHideSearch(Boolean i, Boolean bShouldUpdateQuery, View... root)
    {
        //Search Bar
        SearchView search;
        if (root.length == 0) {
            search = getView().findViewById(R.id.searchCocktails);
        }
        else
        {
            search = root[0].findViewById(R.id.searchCocktails);
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
            if (!bShouldUpdateQuery) {
                //If there is a query, set it to ""
                search.setQuery("", true);
                //To not show keyboard:
                search.clearFocus();
            }
            params.height = 0;
        }
        search.setLayoutParams(params);

    }
}