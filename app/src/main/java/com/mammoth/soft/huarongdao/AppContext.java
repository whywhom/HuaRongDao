package com.mammoth.soft.huarongdao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.mammoth.soft.huarongdao.utils.AppDatabase;
import com.mammoth.soft.huarongdao.utils.CommonFuncsUtils;

public class AppContext extends Application implements Application.ActivityLifecycleCallbacks {
    public static String sharedPF = "HrdSharedPreferences";
    public static String FIRSTSTART = "isFirstInApp";
    public static String LEVEL_UNLOCK = "level_unlock";
    public static String MUSIC = "Music";
    public static String SOUND = "Sound";
    public static String MODE_DAY_NIGHT = "mode_day_night";
    //    public static SharedPreferences sp = null;
    public static String player = "";
    //    private SoundPool soundpool;
//    private int id;
    private static AppDatabase db;
    private static boolean bNight = false;

    private static AppContext singleApplicationInstance;
    // The current App Activity
    private Activity currentActivity = null;


    @Override
    public void onCreate() {
        singleApplicationInstance = this;
        registerActivityLifecycleCallbacks(this);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "hrd_db").build();
        bNight = CommonFuncsUtils.getDayNightModeSet(this, false);
        super.onCreate();
    }

    static public boolean getDayNightMode() {
        return bNight;
    }

    static public AppDatabase getGameDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "hrd_db").build();
        }
        return db;
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    public static AppContext getInstance() {
        return singleApplicationInstance;
    }


    public void showRating() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Toast.makeText(this, "Task Successful", Toast.LENGTH_SHORT).show();
                if (currentActivity != null) {
                    Task<Void> flow = manager.launchReviewFlow(currentActivity, reviewInfo);
                    flow.addOnCompleteListener(task1 -> Toast.makeText(this, "onComplete", Toast.LENGTH_SHORT).show());
                    flow.addOnSuccessListener(result -> {
                        Toast.makeText(this, "onSuccess", Toast.LENGTH_SHORT).show();
                    });
                    flow.addOnFailureListener(e -> {
                        Toast.makeText(this, "onFailure", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                // There was some problem, log or handle the error code.
                Toast.makeText(this, "Task is failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (!activity.equals(currentActivity)) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if (activity.equals(currentActivity)) {
            currentActivity = null;
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
