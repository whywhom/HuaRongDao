package com.whywhom.soft.huarongdao.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SplashViewModel extends ViewModel {
    private MutableLiveData<Boolean> trigger;

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
