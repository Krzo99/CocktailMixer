package com.dKrzmanc.coctailmixer.ui.appinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ui.alctypes.AlcTypesViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;
    public RecyclerView.Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_appcredits, container, false);
        return root;
    }
}
