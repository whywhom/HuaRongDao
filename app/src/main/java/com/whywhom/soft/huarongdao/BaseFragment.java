package com.whywhom.soft.huarongdao;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private Unbinder viewUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        viewUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getLayoutRes();

    @Override
    public void onDestroyView() {
        if (viewUnbinder != null) viewUnbinder.unbind();

        super.onDestroyView();
    }
}
