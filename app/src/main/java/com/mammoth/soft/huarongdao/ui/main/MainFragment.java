package com.mammoth.soft.huarongdao.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.ui.help.HelpFragment;
import com.mammoth.soft.huarongdao.ui.home.GatewayFragment;
import com.mammoth.soft.huarongdao.ui.setting.SettingFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends Fragment {

//    private MainViewModel mViewModel;
//    @BindView(R.id.btn_gamestart) private Button btnGamestart;
//    @BindView(R.id.btn_help) private Button btnGamehelp;
//    @BindView(R.id.btn_gamesetting) private Button btnGamesetting;
    private static MainFragment fragment = null;
    public static MainFragment getInstance() {
        if(fragment == null) {
            fragment = new MainFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(MainFragment.class.getSimpleName(), MainFragment.class.getSimpleName() + "onDestroy");
    }

    @OnClick(R.id.btn_gamestart)
    public void onGameStart(){
        ((MainActivity)getActivity()).presentFragment(GatewayFragment.getInstance(),false);
    }
    @OnClick(R.id.btn_help)
    public void onGameHelp(){
        ((MainActivity)getActivity()).presentFragment(HelpFragment.getInstance(),false);
    }
    @OnClick(R.id.btn_gamesetting)
    public void onGameSetting(){
        ((MainActivity)getActivity()).presentFragment(SettingFragment.getInstance(),false);
    }
}
