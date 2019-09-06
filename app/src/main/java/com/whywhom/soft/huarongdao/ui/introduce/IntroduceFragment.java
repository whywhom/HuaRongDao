package com.whywhom.soft.huarongdao.ui.introduce;

import android.content.Intent;
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
import com.whywhom.soft.huarongdao.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IntroduceFragment extends Fragment {
    private Unbinder viewUnbinder;
    @BindView(R.id.caocao) TextView tv_cc;
    @BindView(R.id.guanyu) TextView tv_gy;
    @BindView(R.id.zhangfei) TextView tv_zf;
    @BindView(R.id.zhaoyun) TextView tv_zy;
    @BindView(R.id.machao) TextView tv_mc;
    @BindView(R.id.huangzhong) TextView tv_hz;

    @BindView(R.id.caocao_detail) TextView tv_ccjs;
    @BindView(R.id.guanyu_detail) TextView tv_gyjs;
    @BindView(R.id.zhangfei_detail) TextView tv_zfjs;
    @BindView(R.id.zhaoyun_detail) TextView tv_zyjs;
    @BindView(R.id.machao_detail) TextView tv_mcjs;
    @BindView(R.id.huangzhong_detail) TextView tv_hzjs;

    boolean bCaoco = false, bGuanyu = false, bZhangfei = false,
            bZhaoyun = false, bMachao = false, bHuangzhong = false;
    private IntroduceViewModel introduceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        introduceViewModel =
                ViewModelProviders.of(this).get(IntroduceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_introduce, container, false);
        viewUnbinder = ButterKnife.bind(this,root);
        introduceViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        if (viewUnbinder != null) viewUnbinder.unbind();
        super.onDestroyView();
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