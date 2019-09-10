package com.whywhom.soft.huarongdao.ui.introduce;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IntroduceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IntroduceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}