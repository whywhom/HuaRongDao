package com.whywhom.soft.huarongdao.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.whywhom.soft.huarongdao.utils.GameHRD;
import com.whywhom.soft.huarongdao.utils.GameLevels;

import java.util.ArrayList;

public class GatewayViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<GameHRD>> hrdList;
    private ArrayList<GameHRD> hrds = new ArrayList<GameHRD>();
//    private MutableLiveData<ArrayList<String>> dataList;
//    private ArrayList<String> data = new ArrayList<String>();

    public GatewayViewModel(@NonNull Application application) {
        super(application);
        hrdList = new MutableLiveData<ArrayList<GameHRD>>();
        initData(application);
    }

    private void initData(Application application) {
        hrds.clear();
        if(GameLevels.chessNameArray != null){
            for(int i = 0; i< GameLevels.chessNameArray.length; i++){
                String name = application.getApplicationContext().getString(GameLevels.chessNameArray[i]);
                GameHRD gameHRD = new GameHRD();
                gameHRD.sethId(i);
                gameHRD.sethName(name);
                gameHRD.sethLocked(i==0?false:true);
                hrds.add(gameHRD);
            }
            hrdList.postValue(hrds);
        }
    }
    public MutableLiveData<ArrayList<GameHRD>> getData() {
        return hrdList;
    }
}