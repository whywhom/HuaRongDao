package com.whywhom.soft.huarongdao.activity;

import com.whywhom.soft.huarongdao.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class IntroduceActivity extends Activity {
	TextView tv_cc;
	TextView tv_gy;
	TextView tv_zf;
	TextView tv_zy;
	TextView tv_mc;
	TextView tv_hz;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduce);
		tv_cc = (TextView)findViewById(R.id.caocao);
		tv_cc.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(IntroduceActivity.this,
						DetailActivity.class);
				intent.putExtra("ID", R.string.caocaojs);
				startActivity(intent);
			}
			
		});
		tv_gy = (TextView)findViewById(R.id.guanyu);
		tv_gy.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(IntroduceActivity.this,
						DetailActivity.class);
				intent.putExtra("ID", R.string.guanyujs);
				startActivity(intent);
			}
			
		});
		tv_zf = (TextView)findViewById(R.id.zhangfei);
		tv_zf.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(IntroduceActivity.this,
						DetailActivity.class);
				intent.putExtra("ID", R.string.zhangfeijs);
				startActivity(intent);
			}
			
		});
		tv_zy = (TextView)findViewById(R.id.zhaoyun);
		tv_zy.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(IntroduceActivity.this,
						DetailActivity.class);
				intent.putExtra("ID", R.string.zhaoyunjs);
				startActivity(intent);
			}
			
		});
		tv_mc = (TextView)findViewById(R.id.machao);
		tv_mc.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(IntroduceActivity.this,
						DetailActivity.class);
				intent.putExtra("ID", R.string.machaojs);
				startActivity(intent);
			}
			
		});
		tv_hz = (TextView)findViewById(R.id.huangzhong);
		tv_hz.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(IntroduceActivity.this,
						DetailActivity.class);
				intent.putExtra("ID", R.string.huangzhongjs);
				startActivity(intent);
			}
			
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			//点后退键的时候,为了防止点得过快,触发两次后退事件,故做此设置.
			if(event.getRepeatCount() == 0){
				IntroduceActivity.this.finish(); 
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
