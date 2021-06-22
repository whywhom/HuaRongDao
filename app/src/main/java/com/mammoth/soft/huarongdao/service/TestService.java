package com.mammoth.soft.huarongdao.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.mammoth.soft.huarongdao.AppContext;

public class TestService extends Service {
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppContext.getInstance().showRating();
        return super.onStartCommand(intent, flags, startId);
    }
}
