package com.dKrzmanc.coctailmixer.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.dKrzmanc.coctailmixer.MainActivity;
import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ui.alctypes.AlcTypesFragment;
import com.dKrzmanc.coctailmixer.ui.make_own.MakeOwnFragment;
import com.dKrzmanc.coctailmixer.ui.recipes.RecipesFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends DialogFragment {
    SharedPreferences mSharedPref;
    MainActivity MainActivity;

    public SettingsFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SettingsFragment newInstance(String title) {
        SettingsFragment frag = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);


        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.settings_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Switch ShowWithOneMissingSwitch = view.findViewById(R.id.ShowOneWIthMissingSwitch);
        final Switch RumCountsAsAllSwitch = view.findViewById(R.id.SettingsCountRumAsAllSwitch);
        final Switch EnableSearchBarSwitch = view.findViewById(R.id.SettingsEnableSearchBarSwitch);

        Button ApplyButton = view.findViewById(R.id.SettingsApplyButton);
        Button CancelButton = view.findViewById(R.id.SettingsCancelButton);
        try {
            mSharedPref = getActivity().getPreferences(MODE_PRIVATE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        boolean bShowOneMissing = mSharedPref.getBoolean("CoctailMixerbShowOneMissing", false);
        ShowWithOneMissingSwitch.setChecked(bShowOneMissing);
        boolean bCountRum = mSharedPref.getBoolean("CoctailMixerbCountRum", false);
        RumCountsAsAllSwitch.setChecked(bCountRum);
        boolean bEnableSearch = mSharedPref.getBoolean("CocktailMixerbEnableSearch", false);
        EnableSearchBarSwitch.setChecked(bEnableSearch);

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible()) {
                    dismiss();
                }
            }
        });

        ApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible())
                {
                    boolean bShowOneMissing = ShowWithOneMissingSwitch.isChecked();
                    boolean bCountRum = RumCountsAsAllSwitch.isChecked();
                    boolean bEnableSearch =  EnableSearchBarSwitch.isChecked();

                    mSharedPref.edit().putBoolean("CoctailMixerbShowOneMissing", bShowOneMissing).apply();
                    mSharedPref.edit().putBoolean("CoctailMixerbCountRum", bCountRum).apply();
                    mSharedPref.edit().putBoolean("CocktailMixerbEnableSearch", bEnableSearch).apply();

                    MainActivity = (MainActivity)getActivity();
                    Fragment navHostFragment = MainActivity.getSupportFragmentManager().getPrimaryNavigationFragment();
                    Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                    if (fragment.getClass() == MakeOwnFragment.class)
                    {
                        ((MakeOwnFragment)fragment).CalcWYCMCocktails();
                    }
                    else if (fragment.getClass() == RecipesFragment.class)
                    {
                        ((RecipesFragment)fragment).ShowHideSearch(bEnableSearch, false);
                    }
                    else if (fragment.getClass() == AlcTypesFragment.class)
                    {
                        ((AlcTypesFragment)fragment).ShowHideSearch(bEnableSearch);
                    }


                    dismiss();
                }
            }
        });
        // Get field from view
        /*mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/
    }
   /* @Override
    public Dialog onCreateDialog(Bundle SavedInstanceState)
    {
        super.onCreateDialog(SavedInstanceState);
        String title = getArguments().getString("Settings");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });

        return alertDialogBuilder.create();
    }*/
}
