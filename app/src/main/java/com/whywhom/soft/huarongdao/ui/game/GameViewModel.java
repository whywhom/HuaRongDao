package com.whywhom.soft.huarongdao.ui.game;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.utils.CommonFuncs;
import com.whywhom.soft.huarongdao.utils.GameHRD;

public class GameViewModel extends ViewModel {
    public void saveRecord(Context context, int level, int total_step) {
        new Thread( new Runnable() {
            public void run() {
                long id = 0;
                GameHRD gameHRD = CommonFuncs.listGameHRD.get(level);
                int record = gameHRD.gethRecord();
                if(total_step<record){
                    gameHRD.sethRecord(total_step);
                    gameHRD.sethStep(0);
                    AppContext.getGameDatabase(context).gameHRDDao().updateGame(gameHRD);
                }
                if(id < 0){
                    Log.e("GameView", "write to database err!");
                }
            }
        }).start();
    }

    public void saveCurrentChessBoard(Context context, int level, int step, int[][] currentChessBoard) {
        new Thread(
            new Runnable(){
                @Override
                public void run() {
                    GameHRD gameHRD = CommonFuncs.listGameHRD.get(level);
                    gameHRD.setIntegerMap(currentChessBoard);
                    gameHRD.sethStep(step);
                    AppContext.getGameDatabase(context).gameHRDDao().updateGame(gameHRD);
                }
            }
        ).start();
    }
}
