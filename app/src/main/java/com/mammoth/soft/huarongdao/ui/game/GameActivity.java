package com.mammoth.soft.huarongdao.ui.game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.utils.CommonFuncsUtils;
import com.mammoth.soft.huarongdao.utils.GameHRD;

public class GameActivity extends AppCompatActivity {
    private int level = 0;
    private Fragment f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent != null){
            level = intent.getIntExtra("level",0);
            this.setTitle(CommonFuncsUtils.listGameHRD.get(level).hName);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        f = GameFragment.getInstance(level);
        transaction.replace(R.id.game_container, f)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.game_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                GameHRD gameHRD = CommonFuncsUtils.listGameHRD.get(level);
                if(((GameFragment)f).getCurrentStep() != gameHRD.step) {
                    showInfoDialog(android.R.id.home, R.string.warn, R.string.exit);
                }else{
                    GameActivity.this.finish();
                }
                break;
            case R.id.nav_refresh:
                showInfoDialog(R.id.nav_refresh, R.string.warn, R.string.retry);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(event.getRepeatCount() == 0){
                    GameHRD gameHRD = CommonFuncsUtils.listGameHRD.get(level);
                    if(((GameFragment)f).getCurrentStep() != gameHRD.step) {
                        showInfoDialog(android.R.id.home, R.string.warn, R.string.exit);
                    } else{
                        GameActivity.this.finish();
                    }
                }
                break;
            default:
                return super.onKeyUp(keyCode, event);
        }
        return true;
    }

    private void showInfoDialog(int id, int titleInfo, int resInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        AlertDialog dialog = builder.setTitle(titleInfo)
                .setMessage(resInfo)
                .setPositiveButton(R.string.bt_yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch(id){
                                    case android.R.id.home:
                                        GameActivity.this.finish();
                                        break;
                                    case R.id.nav_refresh: {
                                        if(f instanceof GameFragment){
                                            ((GameFragment)f).reset();
                                        }
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        })
                .setNegativeButton(R.string.bt_no,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }
}
