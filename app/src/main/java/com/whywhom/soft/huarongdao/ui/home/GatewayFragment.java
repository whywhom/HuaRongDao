package com.whywhom.soft.huarongdao.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whywhom.soft.huarongdao.R;
import com.whywhom.soft.huarongdao.adapter.LevelAdapter;
import com.whywhom.soft.huarongdao.ui.activity.GameActivity;
import com.whywhom.soft.huarongdao.ui.activity.MainActivity;
import com.whywhom.soft.huarongdao.utils.GameHRD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GatewayFragment extends Fragment implements LevelAdapter.OnItemClickListener {
    private Unbinder viewUnbinder;
    private GatewayViewModel gatewayViewModel;
    private LevelAdapter levelAdapter;
    private static GatewayFragment fragment = null;
    @BindView(R.id.rv_level) RecyclerView recyclerView;

    public static GatewayFragment getInstance() {
        if(fragment == null) {
            fragment = new GatewayFragment();
        }
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        gatewayViewModel =
                ViewModelProviders.of(this).get(GatewayViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewUnbinder = ButterKnife.bind(this,root);
        gatewayViewModel.getData().observe(this, new Observer<ArrayList<GameHRD>>() {
            @Override
            public void onChanged(@Nullable ArrayList<GameHRD> s) {
                if(levelAdapter == null){
                    levelAdapter = new LevelAdapter(GatewayFragment.this.getContext(), GatewayFragment.this, gatewayViewModel.getData());
                    GridLayoutManager mgr=new GridLayoutManager(GatewayFragment.this.getContext(),3);
                    recyclerView.setLayoutManager(mgr);
                    //设置适配器
                    recyclerView.setAdapter(levelAdapter);
                } else {
                    levelAdapter.notifyDataSetChanged();
                }
            }
        });
        //navigation 返回重新执行onCreateView，数据不更新，因此需要重新处理recyclerView，否则不显示。
        if(levelAdapter != null){
            GridLayoutManager mgr=new GridLayoutManager(GatewayFragment.this.getContext(),3);
            recyclerView.setLayoutManager(mgr);
            recyclerView.setAdapter(levelAdapter);
        }
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onDestroyView() {
        if (viewUnbinder != null) viewUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), GameActivity.class);
        intent.putExtra("level", position);
        startActivity(intent);
    }
}