package com.android.why.huarongdao.activity;

import com.android.why.huarongdao.R;
import com.android.why.huarongdao.R.id;
import com.android.why.huarongdao.R.layout;
import com.android.why.huarongdao.R.string;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class DetailActivity  extends Activity{
	TextView tv = null;
	int id = R.string.caocaojs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		Intent intent = getIntent();
		if(intent != null){
			id = intent.getIntExtra("ID",R.string.caocaojs);
		}
		tv = (TextView)findViewById(R.id.tv);
		tv.setText(getString(id));
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
				DetailActivity.this.finish(); 
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}