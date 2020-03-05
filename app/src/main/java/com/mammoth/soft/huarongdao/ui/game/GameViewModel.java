package com.mammoth.soft.huarongdao.ui.game;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.mammoth.soft.huarongdao.AppContext;
import com.mammoth.soft.huarongdao.utils.CommonFuncsUtils;
import com.mammoth.soft.huarongdao.utils.GameHRD;

public class GameViewModel extends ViewModel {
    public void saveRecord(Context context, int level, int total_step, boolean bWin) {
        new Thread( new Runnable() {
            public void run() {
                long id = 0;
                GameHRD gameHRD = CommonFuncsUtils.listGameHRD.get(level);
                int record = gameHRD.gethRecord();
                if(bWin){
                    gameHRD.restoreMap(gameHRD.gethMap());
                    gameHRD.sethStep(0);
                }
                if(record<0 && total_step>0){
                    gameHRD.sethRecord(total_step);
                } else if(total_step<record){
                    gameHRD.sethRecord(total_step);
                }
                AppContext.getGameDatabase(context).gameHRDDao().updateGame(gameHRD);
                if(level< CommonFuncsUtils.listGameHRD.size()-1){
                    GameHRD gameHRDUnlock = CommonFuncsUtils.listGameHRD.get(level+1);
                    gameHRDUnlock.sethLocked(false);
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
                    GameHRD gameHRD = CommonFuncsUtils.listGameHRD.get(level);
                    gameHRD.setIntegerMap(currentChessBoard);
                    gameHRD.sethStep(step);
                    AppContext.getGameDatabase(context).gameHRDDao().updateGame(gameHRD);
                }
            }
        ).start();
    }
}
