package com.whywhom.soft.huarongdao.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.whywhom.soft.huarongdao.R;
import com.whywhom.soft.huarongdao.ui.detail.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DetailFragment.newInstance())
                    .commitNow();
        }
    }
}
