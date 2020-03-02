package com.whywhom.soft.huarongdao.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.R;
import com.whywhom.soft.huarongdao.ui.game.GameFragment;
import com.whywhom.soft.huarongdao.ui.help.HelpActivity;
import com.whywhom.soft.huarongdao.ui.help.HelpFragment;
import com.whywhom.soft.huarongdao.ui.home.GatewayFragment;
import com.whywhom.soft.huarongdao.ui.main.MainFragment;
import com.whywhom.soft.huarongdao.ui.setting.SettingFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp = null;
    private boolean bSound = false;
    private boolean bMusic = false;
    private int presentedFragmentID;
    private Fragment f = null;
    private static final String TAG_PRESENTED_FRAGMENT = "tag_presentedFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            presentFragment(GatewayFragment.getInstance(),false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void presentFragment(Fragment fragment, boolean animated){
        if(f != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (animated) {
//            transaction.setCustomAnimations(R.anim.slide_up_in, R.anim.stay, R.anim.stay, R.anim.slide_down_out);
            }
            transaction.add(R.id.host_fragment, fragment, fragment.getClass().getSimpleName());
            if(f != null) {
                transaction.remove(f);
            }
//                    .addToBackStack(null)
            presentedFragmentID = transaction.commit();
            f = fragment;
            Log.d("Fragment ID", Integer.toString(presentedFragmentID));
            if (fragment instanceof SettingFragment) {
                getSupportActionBar().setTitle(R.string.menu_gamesetting);
            } else if (fragment instanceof HelpFragment) {
                getSupportActionBar().setTitle(R.string.menu_help);
            } else {
                getSupportActionBar().setTitle(R.string.app_name);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(f != null) {
            if (f instanceof MainFragment) {
                f.getActivity().finish();
                super.onBackPressed();
            } else{
                presentFragment(GatewayFragment.getInstance(), false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                presentFragment(GatewayFragment.getInstance(), false);
                return true;
            }
            case R.id.nav_help: {
                presentFragment(HelpFragment.getInstance(), false);
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, HelpActivity.class);
//                startActivity(intent);
                return true;
            }
            case R.id.nav_setting: {
                presentFragment(SettingFragment.getInstance(), false);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
