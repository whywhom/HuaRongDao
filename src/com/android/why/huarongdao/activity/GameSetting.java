package com.android.why.huarongdao.activity;

import com.android.why.huarongdao.AppContext;
import com.android.why.huarongdao.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GameSetting extends Activity {
	private Button btnOk = null;
	private EditText name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_setting);
		btnOk = (Button) findViewById(R.id.dl);
		
		name = (EditText) findViewById(R.id.name);
		btnOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String playName = name.getText().toString();
				if(playName == null 
						|| playName.equals("")){
					Toast.makeText(GameSetting.this, GameSetting.this.getString(R.string.username_tip), Toast.LENGTH_LONG).show();
					return;
				}
				if(AppContext.sp.getBoolean(AppContext.FIRSTSTART, true)){
					Editor editor = AppContext.sp.edit();
			        editor.putBoolean(AppContext.FIRSTSTART, false);
			        editor.putString(AppContext.PLAYNAME, playName);
			        editor.commit();
				}
				GameSetting.this.startActivity(new Intent(GameSetting.this, MainActivity.class));
				GameSetting.this.finish();
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

}
