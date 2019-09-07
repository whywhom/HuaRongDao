package com.whywhom.soft.huarongdao.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.whywhom.soft.huarongdao.AppContext;

import static android.content.Context.MODE_PRIVATE;

public class SplashViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> trigger;
    private Application application;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<Boolean> getTrigger() {
        if(trigger == null){
            trigger = new MutableLiveData<Boolean>();
            trigger.postValue(false);
            process();
        }
        return trigger;
    }

    private void process(){
        new Thread( new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    trigger.postValue(true);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
