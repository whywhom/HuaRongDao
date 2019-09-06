package com.whywhom.soft.huarongdao.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.whywhom.soft.huarongdao.utils.GameLevels;

import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> dataList;
    private ArrayList<String> data = new ArrayList<String>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        dataList = new MutableLiveData<ArrayList<String>>();
        initData(application);
    }

    private void initData(Application application) {
        data.clear();
        if(GameLevels.chessNameArray != null){
            for(int i = 0; i< GameLevels.chessNameArray.length; i++){
                data.add(application.getApplicationContext().getString(GameLevels.chessNameArray[i]));
            }
            dataList.postValue(data);
        }
    }
    public MutableLiveData<ArrayList<String>> getData() {
        return dataList;
    }
}