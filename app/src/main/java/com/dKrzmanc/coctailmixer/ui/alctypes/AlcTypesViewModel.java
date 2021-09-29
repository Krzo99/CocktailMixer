package com.dKrzmanc.coctailmixer.ui.alctypes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlcTypesViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public AlcTypesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is AlcTypes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
