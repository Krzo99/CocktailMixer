// Galery = Make your own!

package com.dKrzmanc.coctailmixer.ui.make_own;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dKrzmanc.coctailmixer.ui.recipes.CocktailListAdapter;
import com.dKrzmanc.coctailmixer.ui.recipes.CocktailListItem;
import com.dKrzmanc.coctailmixer.Ingredients;
import com.dKrzmanc.coctailmixer.IngredientsListAdapter;
import com.dKrzmanc.coctailmixer.MainActivity;
import com.dKrzmanc.coctailmixer.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

public class MakeOwnFragment extends Fragment {

    private MakeOwnViewModel makeOwnViewModel;
    AutoCompleteTextView AutoComplete;
    MainActivity Activity;
    ArrayList IngList;
    ArrayList<CocktailListItem> CocktailsWCYM;
    TextView WCYMText;
    RecyclerView.Adapter IngAdapter;
    RecyclerView.Adapter AdapterWYCM;

    SharedPreferences mSavedData;
    boolean bCountRumAsAll;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        makeOwnViewModel =
                ViewModelProviders.of(this).get(MakeOwnViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mixown, container, false);

        AutoComplete = root.findViewById(R.id.AutoCompleteIngredients);
        Activity = (MainActivity)getActivity();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Activity, android.R.layout.select_dialog_item, Activity.AllIngredients);

        AutoComplete.setThreshold(1);
        AutoComplete.setAdapter(adapter);
        IngList = new ArrayList<>();
        CocktailsWCYM = new ArrayList<>();
        mSavedData = getActivity().getPreferences(MODE_PRIVATE);

        //Ingredients list
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.Mix_List);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        IngAdapter = new IngredientsListAdapter(IngList, this);
        recyclerView.setAdapter(IngAdapter);

        //What you can make list
        RecyclerView WCYMRecycler = (RecyclerView) root.findViewById(R.id.WhatCanYouMakeList);
        WCYMRecycler.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManagerWCYM = new LinearLayoutManager(getActivity());
        WCYMRecycler.setLayoutManager(layoutManagerWCYM);



        //CoctailsWCYM.add(new CocktailListItem("Gin tonic", "3:Gin;5:Tonic"));
        AdapterWYCM = new CocktailListAdapter(CocktailsWCYM, this);
        WCYMRecycler.setAdapter(AdapterWYCM);
        ((CocktailListAdapter)AdapterWYCM).filter("");

        WCYMText = root.findViewById(R.id.TextWCYM);
        WCYMText.setText("You can't make anything yet!");

        LoadFromCache();

        ImageView AddIng = root.findViewById(R.id.AddButton);
        AddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ing = AutoComplete.getText().toString();
                if (Activity.AllIngredients.contains(Ing))
                {
                    if (!IngList.contains(Ing)) {
                        AddToIngList(Ing);
                        UpdateCache();
                    }
                    else {
                        Toast toast=Toast.makeText(getContext(),"Ingredient is already on the list!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    AutoComplete.setText("");
                }
                else
                {
                    Toast toast=Toast.makeText(getContext(),"Select a valid ingredient!",Toast.LENGTH_SHORT);
                    toast.show();
                }

                CalcWYCMCocktails();
            }

        });
        AddIng.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int size = IngList.size();
                IngList.clear();
                IngAdapter.notifyItemRangeRemoved(0, size);
                IngAdapter.notifyItemRangeChanged(0, size);

                UpdateCache();
                CalcWYCMCocktails();

                return true;
            }
        });

        return root;
    }

    void AddToIngList(String Ing)
    {
        IngList.add(Ing);
        IngAdapter.notifyItemInserted(IngList.size() - 1);
    }

    public void CalcWYCMCocktails()
    {
        ArrayList<Ingredients> MissingIngs;

        boolean bAllowOneMissing = mSavedData.getBoolean("CoctailMixerbShowOneMissing", false);
        boolean bCoutRumAsOne = mSavedData.getBoolean("CoctailMixerbCountRum", false);
        ArrayList<CocktailListItem> Cocktails = new ArrayList<CocktailListItem>(((MainActivity)getActivity()).CocktailList);
        for (CocktailListItem i : Cocktails) {
            int AmmountOfIngs = i.Ings.size();
            MissingIngs = new ArrayList<>(i.Ings);

            for (Ingredients ing : i.Ings)
            {
                boolean bIsValid = bCoutRumAsOne && (IngList.contains("Rum") && ing.Name.equals("White rum")) || (IngList.contains("Rum") && ing.Name.equals("Dark rum"));
                if (IngList.contains(ing.Name) || bIsValid)
                {
                    MissingIngs.remove(ing);
                    AmmountOfIngs--;
                }
            }

            //Za fully made cocktails
            if (!bAllowOneMissing) {
                if (AmmountOfIngs <= 0 && !CocktailsWCYM.contains(i)) {
                    CocktailsWCYM.add(i);
                    AdapterWYCM.notifyItemInserted(CocktailsWCYM.size() - 1);

                } else if (CocktailsWCYM.contains(i) && AmmountOfIngs > 0) {
                    int Where = CocktailsWCYM.indexOf(i);
                    CocktailsWCYM.remove(Where);
                    AdapterWYCM.notifyItemRemoved(Where);
                    AdapterWYCM.notifyItemRangeChanged(Where, CocktailsWCYM.size());

                }
            }
            else
            {
                if (AmmountOfIngs <= 0)
                {
                    if (!CocktailsWCYM.contains(i))
                    {
                        CocktailsWCYM.add(i);
                        AdapterWYCM.notifyItemInserted(CocktailsWCYM.size() - 1);
                    }
                    else
                    {
                        i.MissingIng = "";
                        AdapterWYCM.notifyDataSetChanged();
                    }
                }
                else if (AmmountOfIngs == 1)
                {
                    if (!CocktailsWCYM.contains(i)) {
                        CocktailsWCYM.add(i);
                        int index = CocktailsWCYM.indexOf(i);
                        CocktailsWCYM.get(index).MissingIng = MissingIngs.get(0).Name;
                        AdapterWYCM.notifyItemInserted(CocktailsWCYM.size() - 1);
                    }
                    else if (!MissingIngs.get(0).equals(i.MissingIng))
                    {
                        i.MissingIng = MissingIngs.get(0).Name;
                        AdapterWYCM.notifyDataSetChanged();
                    }
                }
                else if (AmmountOfIngs > 1 && CocktailsWCYM.contains(i))
                {
                    int Where = CocktailsWCYM.indexOf(i);
                    CocktailsWCYM.remove(Where);
                    AdapterWYCM.notifyItemRemoved(Where);
                    AdapterWYCM.notifyItemRangeChanged(Where, CocktailsWCYM.size());
                }
            }
        }

        if (CocktailsWCYM.size() > 0)
        {
            WCYMText.setText("You can make the following: ");
        }
        else
        {
            WCYMText.setText("You can't make anything yet!");
        }

        Collections.sort(CocktailsWCYM, new Comparator<CocktailListItem>() {
            @Override
            public int compare(CocktailListItem s1, CocktailListItem s2) {
                return s1.Title.compareToIgnoreCase(s2.Title);
            }
        });

        AdapterWYCM.notifyDataSetChanged();

    }

    void LoadFromCache()
    {
        Gson gson = new Gson();
        if (mSavedData.contains("CoctailMixerIngs")) {
            String json = mSavedData.getString("CoctailMixerIngs", "");
            ArrayList<String> temp = gson.fromJson(json, IngList.getClass());
            for (String i : temp)
            {
                AddToIngList(i);
            }

            CalcWYCMCocktails();
        }
    }

    public void UpdateCache(){

        SharedPreferences.Editor prefsEditor = mSavedData.edit();
        Gson gson = new Gson();
        String json = gson.toJson(IngList);
        prefsEditor.putString("CoctailMixerIngs", json);
        prefsEditor.apply();
    }


}