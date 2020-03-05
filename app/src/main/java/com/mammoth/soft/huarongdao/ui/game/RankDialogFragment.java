package com.mammoth.soft.huarongdao.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.utils.CommonFuncsUtils;
import com.mammoth.soft.huarongdao.utils.GameHRD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RankDialogFragment extends DialogFragment {

//    private RankDialogViewModel mViewModel;
    private Unbinder viewUnbinder;
    @BindView(R.id.title) protected TextView tv_title;
    @BindView(R.id.rank) protected TextView tv_rank;
    @BindView(R.id.btn_ok) protected TextView btnOk;
    private static int level = 0;
    public static RankDialogFragment newInstance(int level) {
        RankDialogFragment.level = level;
        return new RankDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_rank, container, false);
        viewUnbinder = ButterKnife.bind(this,view);
        return view;
    }
    @Override
    public void onDestroyView() {
        if (viewUnbinder != null) viewUnbinder.unbind();
        super.onDestroyView();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(RankDialogViewModel.class);
        // TODO: Use the ViewModel
        GameHRD gameHRD = CommonFuncsUtils.listGameHRD.get(level);
        tv_title.setText(gameHRD.gethName());
        String tip = String.format(getString(R.string.tip_rank),gameHRD.gethRecord());
        tv_rank.setText(tip);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankDialogFragment.this.dismiss();
            }
        });
    }

}
