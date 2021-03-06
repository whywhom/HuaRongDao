package com.mammoth.soft.huarongdao.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.ui.main.MainActivity;
import com.mammoth.soft.huarongdao.utils.CommonFuncsUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with User interaction.
 */
public class SplashActivity extends AppCompatActivity {

//    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

//    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        SplashViewModel mViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        mContentView = findViewById(R.id.fullscreen_content);
        hide();
        mViewModel.trigger.observe(this, trigger -> {
            if(trigger){
                SplashActivity.this.startActivity(new Intent(this, MainActivity.class));
                SplashActivity.this.finish();
            }
        });
        Boolean isFirstInApp = CommonFuncsUtils.getIsFirstInAppSPFS(this,true);
        if(isFirstInApp){
            CommonFuncsUtils.setIsFirstInAppSPFS(this,false);
            mViewModel.getTrigger(this, isFirstInApp);
        }
        else{
            mViewModel.getData(this);
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.post(mHidePart2Runnable);
    }

}
