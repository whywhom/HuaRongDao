package com.whywhom.soft.huarongdao.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceFragmentCompat;

import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.R;

import butterknife.BindView;

public class SettingFragment extends PreferenceFragmentCompat {

    private SettingViewModel settingViewModel;
    private static volatile SettingFragment sSoleInstance;
    private SettingFragment(){
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    public static SettingFragment newInstance() {
        if (sSoleInstance == null) {
            synchronized (SettingFragment.class) {
                if (sSoleInstance == null) sSoleInstance = new SettingFragment();
            }
        }
        return sSoleInstance;
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}