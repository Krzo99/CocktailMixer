package com.dKrzmanc.coctailmixer.ui.alctypes;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dKrzmanc.coctailmixer.CocktailListAdapter;
import com.dKrzmanc.coctailmixer.MainActivity;
import com.dKrzmanc.coctailmixer.R;
import com.dKrzmanc.coctailmixer.ui.alctypes.AlcTypesViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlcTypesFragment extends Fragment {

    private AlcTypesViewModel alcTypesViewModel;
    public RecyclerView.Adapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alcTypesViewModel = ViewModelProviders.of(this).get(AlcTypesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_alc_types, container, false);

        //Alcohol list
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.AlcoholListRecyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new AlcoholListAdapter(((MainActivity)getActivity()).AlcoholList, this);
        recyclerView.setAdapter(mAdapter);

        return root;
    }
}
