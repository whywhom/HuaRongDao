package com.whywhom.soft.huarongdao.ui.game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.whywhom.soft.huarongdao.R;
import com.whywhom.soft.huarongdao.ui.game.GameFragment;
import com.whywhom.soft.huarongdao.utils.CommonFuncs;
import com.whywhom.soft.huarongdao.utils.GameHRD;
import com.whywhom.soft.huarongdao.utils.GameLevels;

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
            this.setTitle(CommonFuncs.listGameHRD.get(level).hName);
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
                GameHRD gameHRD = CommonFuncs.listGameHRD.get(level);
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
                    GameHRD gameHRD = CommonFuncs.listGameHRD.get(level);
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
                                if(id == android.R.id.home) {
                                    GameActivity.this.finish();
                                } else if(id == R.id.nav_refresh){
                                    if(f instanceof GameFragment){
                                        ((GameFragment)f).reset();
                                    }
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
