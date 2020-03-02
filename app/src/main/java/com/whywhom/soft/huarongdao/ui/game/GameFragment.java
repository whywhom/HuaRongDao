package com.whywhom.soft.huarongdao.ui.game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.R;
import com.whywhom.soft.huarongdao.service.AudioService;
import com.whywhom.soft.huarongdao.utils.CommonFuncs;
import com.whywhom.soft.huarongdao.utils.GameLevels;
import com.whywhom.soft.huarongdao.view.HrdView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GameFragment extends Fragment {

    private GameViewModel mViewModel;
    private int level = 0;

    private final static String TAG = "GameActivity";
    private int total_step = 0;
    private SoundPool sp;
    private AudioService audioService;
    private boolean bMusic = false;
    private boolean bSound = false;
    private Intent musicIntent = null;
    private Map<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();
    protected int DATA_OK;
    private Unbinder viewUnbinder;
    @BindView(R.id.tv_step) TextView tv_step;
    @BindView(R.id.tv_exit) TextView tv_exit;
    @BindView(R.id.iv_rank) ImageView iv_rank;
    @BindView(R.id.view1) RelativeLayout view;
    private GameFragment() {

    }
    private GameFragment(int level) {
        this.level = level;
    }

    public static Fragment getInstance(int level) {
        GameFragment  fragment = new GameFragment(level);
        return fragment;
    }

    public static interface OnStepListener {

        void onEvent(int step, boolean bWin);
    }
    public static interface OnSoundListener {

        void onEvent(int step);
    }
    OnStepListener onStepListener = new OnStepListener(){

        @Override
        public void onEvent(int step, boolean bWin) {
            // TODO Auto-generated method stub
            total_step = step;
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    tv_step.setText(GameFragment.this.getResources().getString(R.string.step)+ " "+total_step);
                }
            });
            if(bWin){
                new Thread( new Runnable() {
                    public void run() {
                        SaveRecord(total_step);
                    }
                }).start();
                String a = getResources().getString(R.string.win_msg);
                String b = String.format(a, step);
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        GameFragment.this.getContext());
                builder.setTitle(R.string.win_title)
                        .setMessage(b)
                        .setNegativeButton(R.string.bt_rank,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//                                        Intent intent = new Intent();
//                                        intent.setClass(GameFragment.this.getContext(),
//                                                RankActivity.class);
//                                        intent.putExtra("level", level);
//                                        intent.putExtra("player", AppContext.player);
//                                        intent.putExtra("score", total_step);
//                                        GameFragment.this.startActivity(intent);
//                                        GameFragment.this.getActivity().finish();
                                    }
                                })
                        .setPositiveButton(R.string.bt_ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//						SaveRecord(total_step);
                                        total_step = 0;
                                        GameFragment.this.getActivity().finish();
                                    }


                                }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    };
    OnSoundListener onSoundListener = new OnSoundListener(){

        private int id = 0;

        @Override
        public void onEvent(int step) {
            // TODO Auto-generated method stub
            id  = step;
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    playSound(id);
                }
            });

        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            level = bundle.getInt("level",0);
            Log.d(GameFragment.TAG, String.valueOf(level));
        }
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game, container, false);
        viewUnbinder = ButterKnife.bind(this,root);
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
        mViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSound();
        bMusic = CommonFuncs.getMusicSet(this.getContext(), false);
        bSound = CommonFuncs.getSoundSet(this.getContext(), false);
        if(bMusic){
            musicIntent = new Intent();
            musicIntent.setClass(this.getContext(), AudioService.class);
            getActivity().startService(musicIntent);
            getActivity().bindService(musicIntent, conn, Context.BIND_AUTO_CREATE);
        }

        iv_rank.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				SaveRecord(total_step);
//                Intent intent = new Intent();
//                intent.setClass(GameFragment.this.getContext(),
//                        RankActivity.class);
//                intent.putExtra("level", level);
//                intent.putExtra("player", AppContext.player);
//                intent.putExtra("score", total_step);
//                GameFragment.this.startActivity(intent);
            }

        });

//		RelativeLayout.LayoutParams relLayoutParams =
//				new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        HrdView v = new HrdView(view.getContext(), level, onStepListener, onSoundListener);
//		this.view.addView(v,relLayoutParams);
        this.view.addView(v);
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        releaseSound();
        if(bMusic){
            getActivity().unbindService(conn);
            getActivity().stopService(musicIntent);
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
//        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//        toolbar.setSubtitle(null);
        if(bMusic){
            audioService.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
//        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//        toolbar.setSubtitle(GameLevels.chessNameArray[level]);
        if(bMusic){
            if(audioService != null){
                audioService.onResume();
            }
        }
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(event.getRepeatCount() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GameFragment.this.getContext());
                    AlertDialog dialog = builder.setTitle(R.string.warn)
                            .setMessage(R.string.exit)
                            .setPositiveButton(R.string.bt_yes,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            GameFragment.this.getActivity().finish();
                                        }
                                    })
                            .setNegativeButton(R.string.bt_no,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                }
                break;
            default:
                break;
        }
        return true;
    }
    private void initSound() {
        sp = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap.put(0, sp.load(this.getContext(), R.raw.sound2, 0));
        soundPoolMap.put(1, sp.load(this.getContext(), R.raw.sound3, 0));
    }
    private void playSound(int id) {
        AudioManager am = (AudioManager) this.getActivity()
                .getSystemService(Context.AUDIO_SERVICE);
        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        sp.play(soundPoolMap.get(id), volumnCurrent, volumnCurrent, 0, 0, 1f);
    }

    private void releaseSound(){
        sp.release();
    }
    private long SaveRecord(int total_step) {
        // TODO Auto-generated method stub
        long id = 0;
        if(id < 0){
            Log.e(TAG, "write to database err!");
        }
        return id;
    }
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            audioService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            audioService = ((AudioService.AudioBinder)binder).getService();

        }
    };
}
