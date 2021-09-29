package com.dKrzmanc.coctailmixer.ui.favs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ui.appinfo.AboutViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class FavsFragment extends Fragment {
    private FavsViewModel favsViewModel;
    public RecyclerView.Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favsViewModel = ViewModelProviders.of(this).get(FavsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_appcredits, container, false);
        return root;
    }
}
