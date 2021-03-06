package com.mammoth.soft.huarongdao.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mammoth.soft.huarongdao.AppContext;
import com.mammoth.soft.huarongdao.R;
import com.mammoth.soft.huarongdao.adapter.LevelAdapter;
import com.mammoth.soft.huarongdao.ui.game.GameActivity;
import com.mammoth.soft.huarongdao.ui.main.MainActivity;
import com.mammoth.soft.huarongdao.utils.CommonFuncsUtils;
import com.mammoth.soft.huarongdao.utils.GameHRD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GatewayFragment extends Fragment implements LevelAdapter.OnItemClickListener {
    private Unbinder viewUnbinder;
    private GatewayViewModel gatewayViewModel;
    private LevelAdapter levelAdapter;
    private static GatewayFragment fragment = null;
    @BindView(R.id.rv_level) protected RecyclerView recyclerView;

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
        gatewayViewModel.hrdList.observe(this, new Observer<ArrayList<GameHRD>>() {
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
        gatewayViewModel.initData(GatewayFragment.this.getContext());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(levelAdapter != null) {
            levelAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onDestroyView() {
        if (viewUnbinder != null) viewUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(GatewayFragment.class.getSimpleName(), GatewayFragment.class.getSimpleName() + "onDestroy");
    }

    @Override
    public void onItemClick(View view, int position) {
        GameHRD gameHRD = CommonFuncsUtils.listGameHRD.get(position);
        SharedPreferences preferences = GatewayFragment.this.getContext().getSharedPreferences(AppContext.sharedPF, Context.MODE_PRIVATE);
        if(preferences.getBoolean("level_unlock",false)){  //第二个参数指找不到该key的preference时，返回的默认值
            Intent intent = new Intent();
            intent.setClass(getActivity(), GameActivity.class);
            intent.putExtra("level", position);
            startActivity(intent);
        }
        if(gameHRD.ishLocked()){
            Snackbar sb = Snackbar.make(recyclerView, getString(R.string.unlock_tip), Snackbar.LENGTH_SHORT);
            sb.setAction(getString(R.string.bt_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sb.dismiss();
                        }
                    })
                    .show();
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), GameActivity.class);
            intent.putExtra("level", position);
            startActivity(intent);
        }
    }
}