package com.whywhom.soft.huarongdao.ui.splash;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.utils.AppDatabase;
import com.whywhom.soft.huarongdao.utils.CommonFuncs;
import com.whywhom.soft.huarongdao.utils.GameHRD;
import com.whywhom.soft.huarongdao.utils.GameLevels;

import java.util.ArrayList;

public class SplashViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> trigger = new MutableLiveData<>();
    private Application application;
    private Boolean initData = false;
    public SplashViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void getTrigger(Context context, Boolean isFirstIn) {
        process(context, isFirstIn);
    }

    private void process(Context context, Boolean isFirstIn){
        AppDatabase appDatabase = AppContext.getGameDatabase(context);
        new Thread( new Runnable() {
            public void run() {
                if(isFirstIn) {
                    int vol = 4;//列数
                    int row = 5;//行数
                    if (GameLevels.chessNameArray != null) {
                        for (int i = 0; i < GameLevels.chessNameArray.length; i++) {
                            String map = "";
                            GameHRD gameHRD = new GameHRD();
                            String name = application.getApplicationContext().getString(GameLevels.chessNameArray[i]);
                            gameHRD.sethId(i);
                            gameHRD.sethName(name);
                            gameHRD.sethLocked(i == 0 ? false : true);
                            for (int j = 0; j < vol; j++) {
                                for (int k = 0; k < row; k++) {
                                    int value = GameLevels.chessboardArray[i][j][k];
                                    map += String.valueOf(value) + ",";
                                }
                            }
                            gameHRD.sethMap(map);
                            gameHRD.sethCurrentMap(map);
                            appDatabase.gameHRDDao().insertGame(gameHRD);
                        }
                    }

                }
                if(trigger == null) {
                    trigger = new MutableLiveData<Boolean>();
                }
                CommonFuncs.listGameHRD = new ArrayList<GameHRD>(appDatabase.gameHRDDao().getAll());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                trigger.postValue(true);
            }
        }).start();
    }

    public void getData(Context context) {
        AppDatabase appDatabase = AppContext.getGameDatabase(context);
        new Thread( new Runnable() {
            public void run() {
                if(trigger == null) {
                    trigger = new MutableLiveData<Boolean>();
                }
                CommonFuncs.listGameHRD = new ArrayList<GameHRD>(appDatabase.gameHRDDao().getAll());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                trigger.postValue(true);
            }
        }).start();
    }
}
