package com.whywhom.soft.huarongdao.ui.help;

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
import com.whywhom.soft.huarongdao.ui.activity.MainActivity;
import com.whywhom.soft.huarongdao.ui.main.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HelpFragment extends Fragment {
    private Unbinder viewUnbinder;
    private HelpViewModel helpViewModel;
    private static HelpFragment fragment = null;

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
    public static HelpFragment newInstance() {
        if (sSoleInstance == null) {
            synchronized (HelpFragment.class) {
                if (sSoleInstance == null) sSoleInstance = new HelpFragment();
            }
        }
        return sSoleInstance;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        helpViewModel =
                ViewModelProviders.of(this).get(HelpViewModel.class);
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
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroyView() {
        if (viewUnbinder != null) viewUnbinder.unbind();
        super.onDestroyView();
    }
}