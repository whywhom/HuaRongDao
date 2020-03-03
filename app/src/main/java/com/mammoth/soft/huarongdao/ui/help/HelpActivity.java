package com.mammoth.soft.huarongdao.ui.help;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mammoth.soft.huarongdao.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, HelpFragment.newInstance())
                    .commitNow();
        }
    }
}
