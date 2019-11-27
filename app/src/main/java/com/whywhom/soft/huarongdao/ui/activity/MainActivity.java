package com.whywhom.soft.huarongdao.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.whywhom.soft.huarongdao.R;
import com.whywhom.soft.huarongdao.ui.help.HelpFragment;
import com.whywhom.soft.huarongdao.ui.home.HomeFragment;
import com.whywhom.soft.huarongdao.ui.introduce.IntroduceFragment;
import com.whywhom.soft.huarongdao.ui.setting.SettingFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences sp = null;
    private boolean bSound = false;
    private boolean bMusic = false;
    private Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_setting:

                        break;
                }
                return true;
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_level);
        fragment = HomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_level:
                if(!(fragment instanceof HomeFragment)) {
                    fragment = HomeFragment.newInstance();
                }
                break;
            case R.id.nav_help:
                if(!(fragment instanceof HelpFragment)) {
                    fragment = HelpFragment.newInstance();
                }
                break;
            case R.id.nav_introduce:
                if(!(fragment instanceof IntroduceFragment)) {
                    fragment = IntroduceFragment.newInstance();
                }
                break;
            case  R.id.nav_setting:
                if(!(fragment instanceof SettingFragment)) {
                    fragment = SettingFragment.newInstance();
                }
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
