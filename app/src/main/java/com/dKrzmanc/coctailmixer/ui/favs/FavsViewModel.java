package com.dKrzmanc.coctailmixer.ui.favs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FavsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Favs fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
