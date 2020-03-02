package com.whywhom.soft.huarongdao.ui.home;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.utils.AppDatabase;
import com.whywhom.soft.huarongdao.utils.CommonFuncs;
import com.whywhom.soft.huarongdao.utils.GameHRD;
import com.whywhom.soft.huarongdao.utils.GameLevels;

import java.util.ArrayList;

public class GatewayViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<GameHRD>> hrdList;
    private ArrayList<GameHRD> hrds = new ArrayList<GameHRD>();
//    private MutableLiveData<ArrayList<String>> dataList;
//    private ArrayList<String> data = new ArrayList<String>();

    public GatewayViewModel(@NonNull Application application) {
        super(application);
        hrdList = new MutableLiveData<ArrayList<GameHRD>>();
//        initData(application);
    }

    public void initData(Context context) {
        hrds.clear();
        AppDatabase appDatabase = AppContext.getGameDatabase(context);
        new Thread( new Runnable() {
            public void run() {
                CommonFuncs.listGameHRD = new ArrayList<GameHRD>(appDatabase.gameHRDDao().getAll());
                hrdList.postValue(CommonFuncs.listGameHRD);
            }
        }).start();
    }
    public MutableLiveData<ArrayList<GameHRD>> getData() {
        return hrdList;
    }
}