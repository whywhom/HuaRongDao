package com.mammoth.soft.huarongdao.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceFragmentCompat;

import com.mammoth.soft.huarongdao.AppContext;
import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.ui.main.MainActivity;

public class SettingFragment extends PreferenceFragmentCompat {

    private SettingViewModel settingViewModel;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = super.onCreateView(inflater, container, savedInstanceState);
        settingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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