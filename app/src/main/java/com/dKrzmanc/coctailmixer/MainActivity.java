package com.dKrzmanc.coctailmixer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.dKrzmanc.coctailmixer.ui.alctypes.AlcoholListItem;
import com.dKrzmanc.coctailmixer.ui.recipes.CocktailListItem;
import com.dKrzmanc.coctailmixer.ui.recipes.RecipesFragment;
import com.dKrzmanc.coctailmixer.ui.settings.SettingsFragment;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static final int READ_STORAGE_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    public ArrayList<CocktailListItem> CocktailList;
    public ArrayList<AlcoholListItem> AlcoholList;
    public static ArrayList<String> AlcoholListStrings;
    public static ArrayList<Character> Vowels = new ArrayList<Character>(Arrays.asList('A','E','I','O','U'));

    public ArrayList<String> AllIngredients;
    public boolean bShowEasterEgg = false;
    CocktailListItem EasterEggCocktail;

    public ArrayList<CocktailListItem> FavouriteCocktails;
    public ArrayList<CocktailListItem> NotesChangedCocktails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each of Ids menu ID as a set  because each
        // menu should be considered as top level destinations.
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.nav_recepie);
        topLevelDestinations.add(R.id.nav_favs);
        topLevelDestinations.add(R.id.nav_alc_types);
        topLevelDestinations.add(R.id.nav_appinfo);
        topLevelDestinations.add(R.id.nav_mix);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                topLevelDestinations)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        EasterEggCocktail = new CocktailListItem("Mulled wine", "1:Wine;24:Cinnamon", "It's the most wonderful time of the year", "");


        ImageView Logo = findViewById(R.id.AppLogo);
        Logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                bShowEasterEgg = !bShowEasterEgg;

                Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
                Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

                if (fragment.getClass() == RecipesFragment.class) {
                    RecipesFragment HomeFrag = (RecipesFragment) fragment;
                    if (bShowEasterEgg) {
                        Toast toast = Toast.makeText(view.getContext(), "OwO, what have you done?!?", Toast.LENGTH_SHORT);
                        toast.show();

                        CocktailList.add(EasterEggCocktail);

                        Collections.sort(CocktailList, new Comparator<CocktailListItem>() {
                            @Override
                            public int compare(CocktailListItem s1, CocktailListItem s2) {
                                return s1.Title.compareToIgnoreCase(s2.Title);
                            }
                        });

                        HomeFrag.mAdapter.notifyDataSetChanged();
                    } else {
                        Toast toast = Toast.makeText(view.getContext(), "Back to normal!", Toast.LENGTH_SHORT);
                        toast.show();
                        CocktailList.remove(EasterEggCocktail);

                        HomeFrag.mAdapter.notifyDataSetChanged();
                    }
                }

                return false;
            }
        });

        GenerateAlcoholList();
        InitCocktailRecycleList();

        NotesChangedCocktails = LoadNotesChanged();
        if (NotesChangedCocktails == null) {
            NotesChangedCocktails = new ArrayList<>();
        }

        FavouriteCocktails = LoadFavourites();
        if (FavouriteCocktails == null) {
            FavouriteCocktails = new ArrayList<>();
        }
    }

    private void GenerateAlcoholList() {
        AlcoholList = new ArrayList<>();
        AlcoholListStrings = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.alcohol_types);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] a;

            // Za vse vrstice v csv
            while ((a = reader.readNext()) != null) {
                String Name = a[0];
                String Desc = a[1];

                AlcoholListItem NewItem = new AlcoholListItem(Name, Desc);

                AlcoholListStrings.add(Name.toLowerCase());
                AlcoholList.add(NewItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitCocktailRecycleList() {
        CocktailList = new ArrayList<>();
        AllIngredients = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.cocktails);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] a;
            String Note = "";
            while ((a = reader.readNext()) != null) {
                String Name = a[0];
                String Ing = "";
                Note = "";
                for (int i = 1; i < a.length; i++)
                {
                    if (!a[i].equals(""))
                    {
                        if (!a[i].contains("N:")) {
                            Ing += a[i];
                            Ing += (i % 2 == 0 ? ";" : ":");

                            if (i % 2 == 0 && !AllIngredients.contains(a[i])) {
                                AllIngredients.add(a[i]);
                            }
                        }
                        else
                        {
                            String[] Notes = a[i].split(":");
                            Note = Notes[1];
                        }
                    }

                }
                CocktailListItem NewItem = new CocktailListItem(Name, Ing, Note,"");
                CocktailList.add(NewItem);
            }

            Collections.sort(CocktailList, new Comparator<CocktailListItem>() {
                @Override
                public int compare(CocktailListItem s1, CocktailListItem s2) {
                    return s1.Title.compareToIgnoreCase(s2.Title);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean addCocktailToFav(CocktailListItem ThisItem)
    {
        FavouriteCocktails.add(ThisItem);
        return saveFavs();
    }
    public boolean removeCocktailFromFav(CocktailListItem ThisItem)
    {
        for (int i = 0; i < FavouriteCocktails.size(); i++)
        {
            if(FavouriteCocktails.get(i).equals(ThisItem))
            {
                FavouriteCocktails.remove(i);
                return saveFavs();
            }
        }
        return false;
    }
    //Saves favourites list to memory
    public boolean saveFavs()
    {
        SharedPreferences mSharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        Gson gson = new Gson();


        try {
            String json = gson.toJson(FavouriteCocktails);
            editor.putString("CocktailMixerFavs", json);
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getBaseContext(), "Saving favs failed!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

    }

    //Saves notes list to memory
    public boolean saveNotes()
    {
        SharedPreferences mSharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        Gson gson = new Gson();


        try {
            String json = gson.toJson(NotesChangedCocktails);
            editor.putString("CocktailMixerNotes", json);
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getBaseContext(), "Saving notes failed!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

    }

    public ArrayList<CocktailListItem> LoadFavourites()
    {
        SharedPreferences mSharedPref = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeOfCocktailListArray = new TypeToken<ArrayList<CocktailListItem>>(){}.getType();

        try {
            String json = mSharedPref.getString("CocktailMixerFavs", "");
            if (json.equals("")) {
                return new ArrayList<CocktailListItem>();
            }
            ArrayList<CocktailListItem> LoadedFavs = gson.fromJson(json, typeOfCocktailListArray);
            for (CocktailListItem f : LoadedFavs)
            {
                int indexOfThis = CocktailList.indexOf(f);
                CocktailList.get(indexOfThis).bIsFavourited = true;
            }
            return LoadedFavs;
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getBaseContext(), "Loading favs failed!", Toast.LENGTH_SHORT);
            toast.show();
            return new ArrayList<CocktailListItem>();
        }
    }

    public ArrayList<CocktailListItem> LoadNotesChanged()
    {
        SharedPreferences mSharedPref = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeOfCocktailListArray = new TypeToken<ArrayList<CocktailListItem>>(){}.getType();

        try {
            String json = mSharedPref.getString("CocktailMixerNotes", "");
            if (json.equals("")) {
                return new ArrayList<CocktailListItem>();
            }
            ArrayList<CocktailListItem> LoadedChangedNotes = gson.fromJson(json, typeOfCocktailListArray);
            for (CocktailListItem f : LoadedChangedNotes)
            {
                int indexOfThis = CocktailList.indexOf(f);
                CocktailList.get(indexOfThis).Notes = f.Notes;
            }
            return LoadedChangedNotes;
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getBaseContext(), "Loading Notes failed!", Toast.LENGTH_SHORT);
            toast.show();
            return new ArrayList<CocktailListItem>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
        {
            FragmentManager fm = getSupportFragmentManager();
            SettingsFragment SettingsDialogFragment = SettingsFragment.newInstance("SettingsInstance ofc");
            SettingsDialogFragment.show(fm, "Settings_Dialog_Fragment");
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}