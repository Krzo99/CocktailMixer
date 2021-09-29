package com.dKrzmanc.coctailmixer.ui.make_own;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MakeOwnViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MakeOwnViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}