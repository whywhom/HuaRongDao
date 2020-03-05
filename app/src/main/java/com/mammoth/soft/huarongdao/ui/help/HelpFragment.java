package com.mammoth.soft.huarongdao.ui.help;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.ui.main.MainActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HelpFragment extends Fragment {
    private Unbinder viewUnbinder;
    private static HelpFragment fragment = null;

    @BindView(R.id.caocao) protected TextView tv_cc;
    @BindView(R.id.guanyu) protected TextView tv_gy;
    @BindView(R.id.zhangfei) protected TextView tv_zf;
    @BindView(R.id.zhaoyun) protected TextView tv_zy;
    @BindView(R.id.machao) protected TextView tv_mc;
    @BindView(R.id.huangzhong) protected TextView tv_hz;

    @BindView(R.id.caocao_detail) protected TextView tv_ccjs;
    @BindView(R.id.guanyu_detail) protected TextView tv_gyjs;
    @BindView(R.id.zhangfei_detail) protected TextView tv_zfjs;
    @BindView(R.id.zhaoyun_detail) protected TextView tv_zyjs;
    @BindView(R.id.machao_detail) protected TextView tv_mcjs;
    @BindView(R.id.huangzhong_detail) protected TextView tv_hzjs;
    private boolean bCaoco = false, bGuanyu = false, bZhangfei = false,
            bZhaoyun = false, bMachao = false, bHuangzhong = false;

    public static HelpFragment getInstance() {
        if(fragment == null) {
            fragment = new HelpFragment();
        }
        return fragment;
    }
    @BindView(R.id.text_help) TextView tv;

    private static volatile HelpFragment sSoleInstance;
    private HelpFragment(){
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    static HelpFragment newInstance() {
        if (sSoleInstance == null) {
            synchronized (HelpFragment.class) {
                if (sSoleInstance == null) sSoleInstance = new HelpFragment();
            }
        }
        return sSoleInstance;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HelpViewModel helpViewModel = ViewModelProviders.of(this).get(HelpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_help, container, false);
        viewUnbinder = ButterKnife.bind(this,root);
        tv.setText(getString(R.string.doc));
        helpViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

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
    public void onDestroyView() {
        if (viewUnbinder != null) viewUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(HelpFragment.class.getSimpleName(), HelpFragment.class.getSimpleName() + "onDestroy");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_cc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bCaoco = !bCaoco;
                tv_ccjs.setVisibility(bCaoco? View.VISIBLE:View.GONE);
            }

        });

        tv_gy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bGuanyu = !bGuanyu;
                tv_gyjs.setVisibility(bGuanyu? View.VISIBLE:View.GONE);
            }

        });

        tv_zf.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bZhangfei = !bZhangfei;
                tv_zfjs.setVisibility(bZhangfei? View.VISIBLE:View.GONE);
            }

        });

        tv_zy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bZhaoyun = !bZhaoyun;
                tv_zyjs.setVisibility(bZhaoyun? View.VISIBLE:View.GONE);
            }

        });

        tv_mc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bMachao = !bMachao;
                tv_mcjs.setVisibility(bMachao? View.VISIBLE:View.GONE);
            }

        });

        tv_hz.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bHuangzhong = !bHuangzhong;
                tv_hzjs.setVisibility(bHuangzhong? View.VISIBLE:View.GONE);
            }

        });
    }
}