package com.whywhom.soft.huarongdao.activity;

import com.whywhom.soft.huarongdao.R;
import com.qq.e.splash.SplashAd;
import com.qq.e.splash.SplashAdListener;
import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RSplashActivity  extends Activity {
	private TextView tv = null;
	private boolean bFirst = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		bFirst = AppContext.sp.getBoolean("FirstStart", true);


		tv = (TextView)findViewById(R.id.bottonText);
		tv.setText(getVersion());
		RelativeLayout adsParent = (RelativeLayout) this
				.findViewById(R.id.adsRl);
		
		SplashAd ad = new SplashAd(this, adsParent, Constants.APPId, Constants.SplashPosId,
				new SplashAdListener() {

			@Override
			public void onAdPresent() {
			}

			@Override
			public void onAdFailed(int arg0) {
//				RSplashActivity.this.startActivity(new Intent(RSplashActivity.this, MainActivity.class));
				enterDelay();
			}

			@Override
			public void onAdDismissed() {
//				RSplashActivity.this.startActivity(new Intent(RSplashActivity.this, MainActivity.class));
				enter();
			}
		});
	}

	private void enter() {
		new Thread( new Runnable() {
			public void run() {
				if(bFirst){
					RSplashActivity.this.startActivity(new Intent(RSplashActivity.this, GameSetting.class));
				}else{
					RSplashActivity.this.startActivity(new Intent(RSplashActivity.this, MainActivity.class));
				}
				RSplashActivity.this.finish();
			}
		}).start();
	}
	
	private void enterDelay() {
		new Thread( new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);	
					if(bFirst){
						RSplashActivity.this.startActivity(new Intent(RSplashActivity.this, GameSetting.class));
					}else{
						RSplashActivity.this.startActivity(new Intent(RSplashActivity.this, MainActivity.class));
					}
					RSplashActivity.this.finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	private String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
