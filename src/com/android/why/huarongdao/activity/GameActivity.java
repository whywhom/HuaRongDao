package com.android.why.huarongdao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.why.huarongdao.AppContext;
import com.android.why.huarongdao.R;
import com.android.why.huarongdao.R.id;
import com.android.why.huarongdao.R.layout;
import com.android.why.huarongdao.R.raw;
import com.android.why.huarongdao.R.string;
import com.android.why.huarongdao.service.AudioService;
import com.android.why.huarongdao.service.AudioService.AudioBinder;
import com.android.why.huarongdao.util.GameLevels;
import com.android.why.huarongdao.util.ScoreItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends Activity{
	private int level = 0;
	private RelativeLayout view = null;
	private TextView tv_step = null;
	private TextView tv_exit = null;
	private ImageView iv_rank = null;
	private int total_step = 0;
	private SoundPool sp;
	private AudioService audioService;
	private boolean bMusic = false;
	private boolean bSound = false;
	private Intent musicIntent = null;
	private Map<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();   
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
			runOnUiThread(new Runnable() {
				public void run() {
					tv_step.setText(GameActivity.this.getResources().getString(R.string.step)+ " "+total_step);
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
						GameActivity.this);
				builder.setTitle(R.string.win_title)
				.setMessage(b)
//				.setNeutralButton(R.string.bt_share,
//						new DialogInterface.OnClickListener() {
//	
//					@Override
//					public void onClick(DialogInterface dialog,
//							int which) {
//						
//					}
//				})
				.setNegativeButton(R.string.bt_rank,
						new DialogInterface.OnClickListener() {
	
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
//						SaveRecord(total_step);
						Intent intent = new Intent();
						intent.setClass(GameActivity.this,
								RankActivity.class);
						intent.putExtra("level", level);
						intent.putExtra("player", AppContext.player);
						intent.putExtra("score", total_step);
						GameActivity.this.startActivity(intent);
						GameActivity.this.finish();
					}
				})
				.setPositiveButton(R.string.bt_ok,
						new DialogInterface.OnClickListener() {
	
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
//						SaveRecord(total_step);
						total_step = 0;
						GameActivity.this.finish();
					}

					
				}).show();
			}
		}
		
	};
	OnSoundListener onSoundListener = new OnSoundListener(){

		private int id = 0;

		@Override
		public void onEvent(int step) {
			// TODO Auto-generated method stub
			id  = step;
			runOnUiThread(new Runnable() {
				public void run() {
					playSound(id);
				}
			});
			
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Intent intent = getIntent();
		
		if(intent != null){
			level = intent.getIntExtra("level",0);
			this.setTitle(GameLevels.chessNameArray[level]); 
		}
		this.
		initSound();
		bMusic = AppContext.sp.getBoolean(AppContext.MUSIC, false);
		bSound = AppContext.sp.getBoolean(AppContext.SOUND, false);
		if(bMusic){
			musicIntent = new Intent();  
			musicIntent.setClass(this, AudioService.class);
			startService(musicIntent);  
			bindService(musicIntent, conn, Context.BIND_AUTO_CREATE);  
		}
		tv_step  = (TextView)findViewById(R.id.tv_step);
		tv_exit = (TextView)findViewById(R.id.tv_exit);
		iv_rank = (ImageView)findViewById(R.id.iv_rank);
		iv_rank.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SaveRecord(total_step);
				Intent intent = new Intent();
				intent.setClass(GameActivity.this,
						RankActivity.class);
				intent.putExtra("level", level);
				intent.putExtra("player", AppContext.player);
				intent.putExtra("score", total_step);
				GameActivity.this.startActivity(intent);
//				GameActivity.this.finish();
			}
			
		});
		view = (RelativeLayout)findViewById(R.id.view1);
//		RelativeLayout.LayoutParams relLayoutParams = 
//				new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		HrdView v = new HrdView(view.getContext(), level, onStepListener, onSoundListener);
//		this.view.addView(v,relLayoutParams);
		this.view.addView(v);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		releaseSound();
		if(bMusic){
			unbindService(conn);  
            stopService(musicIntent);  
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(bMusic){
			 audioService.onPause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(bMusic){
			if(audioService != null){
				audioService.onResume();
			}
		}
		super.onResume();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(event.getRepeatCount() == 0){
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GameActivity.this);
				AlertDialog dialog = builder.setTitle(R.string.warn)
				.setMessage(R.string.exit)
				.setPositiveButton(R.string.bt_yes,
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						GameActivity.this.finish();
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
		return super.onKeyDown(keyCode, event);
	}
	private void initSound() {
		sp = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap.put(0, sp.load(this, R.raw.sound2, 0)); 
		soundPoolMap.put(1, sp.load(this, R.raw.sound3, 0)); 
	}
	private void playSound(int id) {
		AudioManager am = (AudioManager) GameActivity.this
					.getSystemService(Context.AUDIO_SERVICE);
		float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		sp.play(soundPoolMap.get(id), volumnCurrent, volumnCurrent, 0, 0, 1f);
	}

	private void releaseSound(){
		sp.release();
	}
	private void SaveRecord(int total_step) {
		// TODO Auto-generated method stub
		ArrayList<ScoreItem> fl1 = AppContext.dbHelper.getItems(getString(GameLevels.chessNameArray[level]));
		if(fl1 == null){
			
		}else{
			for(ScoreItem si:fl1){
				if(si.score == total_step){
					return;
				}
			}
		}
		//save record
		ScoreItem si = new ScoreItem(total_step,AppContext.player,getString(GameLevels.chessNameArray[level]));
		long id = AppContext.dbHelper.saveItems(si);
		if(id < 0){
			
		}
	}
	private ServiceConnection conn = new ServiceConnection() {  
        
        @Override  
        public void onServiceDisconnected(ComponentName name) {  
            // TODO Auto-generated method stub  
            audioService = null;  
        }  
          
        @Override  
        public void onServiceConnected(ComponentName name, IBinder binder) {  
            //杩欓噷鎴戜滑瀹炰緥鍖朼udioService,閫氳繃binder鏉ュ疄鐜�  
            audioService = ((AudioService.AudioBinder)binder).getService();  
              
        }  
    };  
}
