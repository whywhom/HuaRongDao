package com.whywhom.soft.huarongdao.ui.home;

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
import com.whywhom.soft.huarongdao.utils.GameHRD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements LevelAdapter.OnItemClickListener {
    private Unbinder viewUnbinder;
    private HomeViewModel homeViewModel;
    private LevelAdapter levelAdapter;
    @BindView(R.id.rv_level) RecyclerView recyclerView;
    private static volatile HomeFragment sSoleInstance;
    private HomeFragment(){
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    public static HomeFragment newInstance() {
        if (sSoleInstance == null) {
            synchronized (HomeFragment.class) {
                if (sSoleInstance == null) sSoleInstance = new HomeFragment();
            }
        }
        return sSoleInstance;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewUnbinder = ButterKnife.bind(this,root);
        homeViewModel.getData().observe(this, new Observer<ArrayList<GameHRD>>() {
            @Override
            public void onChanged(@Nullable ArrayList<GameHRD> s) {
                if(levelAdapter == null){
                    levelAdapter = new LevelAdapter(HomeFragment.this.getContext(), HomeFragment.this, homeViewModel.getData());
                    GridLayoutManager mgr=new GridLayoutManager(HomeFragment.this.getContext(),3);
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
            GridLayoutManager mgr=new GridLayoutManager(HomeFragment.this.getContext(),3);
            recyclerView.setLayoutManager(mgr);
            recyclerView.setAdapter(levelAdapter);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        if (viewUnbinder != null) viewUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("level", position);
        NavHostFragment.findNavController(this).navigate(R.id.action_page_game,bundle);
    }
}