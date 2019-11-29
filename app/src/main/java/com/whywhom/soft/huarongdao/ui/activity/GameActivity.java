package com.whywhom.soft.huarongdao.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.whywhom.soft.huarongdao.R;
import com.whywhom.soft.huarongdao.ui.game.GameFragment;
import com.whywhom.soft.huarongdao.utils.GameLevels;

public class GameActivity extends AppCompatActivity {
    private int level = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent != null){
            level = intent.getIntExtra("level",0);
            this.setTitle(GameLevels.chessNameArray[level]);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_container, GameFragment.getInstance(level))
                .addToBackStack(null)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
