package com.whywhom.soft.huarongdao.activity;

import com.whywhom.soft.huarongdao.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.widget.TextView;

public class HelpActivity extends Activity{
	TextView tv = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		tv = (TextView)findViewById(R.id.tv);
		tv.setText(getString(R.string.doc));
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
	            HelpActivity.this.finish(); 
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
