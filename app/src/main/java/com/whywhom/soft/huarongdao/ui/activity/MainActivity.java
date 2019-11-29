package com.whywhom.soft.huarongdao.ui.activity;

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
import com.whywhom.soft.huarongdao.ui.help.HelpFragment;
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
    private static final String TAG_PRESENTED_FRAGMENT = "tag_presentedFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            presentFragment(MainFragment.getInstance(),false);
        }
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//
//                    case R.id.nav_setting:
//                        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
//                        NavigationUI.onNavDestinationSelected(item, navController);
//                        break;
//                }
//                return true;
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void presentFragment(Fragment fragment, boolean animated){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(animated) {
//            transaction.setCustomAnimations(R.anim.slide_up_in, R.anim.stay, R.anim.stay, R.anim.slide_down_out);
        }
        presentedFragmentID = transaction.replace(R.id.host_fragment, fragment, TAG_PRESENTED_FRAGMENT)
                .addToBackStack(null)
                .commit();
        Log.d("Fragment ID",Integer.toString(presentedFragmentID));
        if(fragment instanceof SettingFragment){
            getSupportActionBar().setTitle(R.string.menu_gamesetting);
        } else if(fragment instanceof HelpFragment){
            getSupportActionBar().setTitle(R.string.menu_help);
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            presentFragment(MainFragment.getInstance(),false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
