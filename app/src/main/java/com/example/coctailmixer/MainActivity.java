package com.example.coctailmixer;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.coctailmixer.ui.settings.SettingsFragment;
import com.opencsv.CSVReader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class MainActivity extends AppCompatActivity {
    //TODO add notes to .csv file!!

    private AppBarConfiguration mAppBarConfiguration;
    private static final int READ_STORAGE_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    public ArrayList<CocktailListItem> CocktailList;
    public ArrayList<String> AllIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_recepie)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        InitCoctailRecycleList();
    }

    private void InitCoctailRecycleList() {
        CocktailList = new ArrayList<>();
        AllIngredients = new ArrayList();
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
            SettingsFragment editNameDialogFragment = SettingsFragment.newInstance("Some Title");
            editNameDialogFragment.show(fm, "fragment_edit_name");
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