package com.mammoth.soft.huarongdao.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.mammoth.soft.huarongdao.AppContext;
import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.ui.main.MainActivity;

import java.util.Objects;

public class SettingFragment extends PreferenceFragmentCompat{

    private static SettingFragment fragment = null;

    public static SettingFragment getInstance() {
        if(fragment == null) {
            fragment = new SettingFragment();
        }
        return fragment;
    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //修改配置名
        getPreferenceManager().setSharedPreferencesName(AppContext.sharedPF);
        //必须放在修改配置名之后
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingViewModel settingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = super.onCreateView(inflater, container, savedInstanceState);
        settingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(SettingFragment.class.getName(), s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(SettingFragment.class.getSimpleName(), SettingFragment.class.getSimpleName() + "onDestroy");
    }
}