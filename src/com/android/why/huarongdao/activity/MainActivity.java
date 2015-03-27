package com.android.why.huarongdao.activity;

import java.util.ArrayList;

import com.android.why.huarongdao.AppContext;
import com.android.why.huarongdao.R;
import com.android.why.huarongdao.R.drawable;
import com.android.why.huarongdao.R.id;
import com.android.why.huarongdao.R.layout;
import com.android.why.huarongdao.R.string;
import com.android.why.huarongdao.util.Constants;
import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private long firstTime = 0;
	private ListView lv;
	private ArrayList<String> dataList = new ArrayList<String>();
	private MainAdapter adapter = null;
	private boolean bMusic = false;
	private boolean bSound = false;
	private RelativeLayout bannerContainer;
	private AdView bannerAD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		this.bannerContainer = (RelativeLayout) this.findViewById(R.id.bannercontainer);
		lv = (ListView) findViewById(R.id.lv_gamemenu);
		AppContext.player = AppContext.sp.getString(AppContext.PLAYNAME, getString(R.string.player));
		adapter = new MainAdapter(this, dataList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch (position) {
				case 0:
					//进入游戏
					intent = new Intent(MainActivity.this,
							LevelsActivity.class);
					startActivity(intent);
					break;
				case 1:
					//游戏介绍
					intent = new Intent(MainActivity.this,
							HelpActivity.class);
					startActivity(intent);
					break;
				case 2:
					//人物介绍
					intent = new Intent(MainActivity.this,
							IntroduceActivity.class);
					startActivity(intent);
					break;
				case 3:
					//游戏设置
//					intent = new Intent(MainActivity.this,
//							GameSetting.class);
//					startActivity(intent);
					bMusic = AppContext.sp.getBoolean(AppContext.MUSIC, false);
					bSound = AppContext.sp.getBoolean(AppContext.SOUND, false);
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	                builder.setIcon(R.drawable.ic_launcher);
	                builder.setTitle(getString(R.string.menu_gamesetting));
	                final String[] hobbies = {getString(R.string.music_setting), getString(R.string.sound_setting)};
	                final boolean [] result = {bMusic,bSound};
	                //    设置一个单项选择下拉框
	                /**
	                 * 第一个参数指定我们要显示的一组下拉多选框的数据集合
	                 * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
	                 * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
	                 * 第三个参数给每一个多选项绑定一个监听器
	                 */
	                builder.setMultiChoiceItems(hobbies, result, new DialogInterface.OnMultiChoiceClickListener()
	                {
	                    StringBuffer sb = new StringBuffer(100);
	                    @Override
	                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
	                    {
	                        if(which == 0){
	                        	Editor editor = AppContext.sp.edit();
	                	        editor.putBoolean(AppContext.MUSIC, isChecked);
	                	        editor.commit();
	                        } else if(which == 1){
	                        	Editor editor = AppContext.sp.edit();
	                	        editor.putBoolean(AppContext.SOUND, isChecked);
	                	        editor.commit();
	                        } else{
	                        	
	                        }
	                    }
	                });
	                builder.setPositiveButton(getString(R.string.bt_ok), new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                        
	                    }
	                });
	                builder.setNegativeButton(getString(R.string.bt_cancel), new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    	dialog.dismiss();
	                    }
	                });
	                builder.show();
					break;
				default:
					break;
				}
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AppContext.dbHelper.close();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.showBannerAD();
	}
	private void initData() {
		dataList.add(getString(R.string.menu_gamestart));
		dataList.add(getString(R.string.menu_help));
		dataList.add(getString(R.string.menu_introduce));
		dataList.add(getString(R.string.menu_gamesetting));
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(event.getRepeatCount() == 0){
				//连按两下退出应用
				long secondTime = System.currentTimeMillis(); 
				if (secondTime - firstTime > 2000) {                                         
					//如果两次按键时间间隔大于2秒，则不退出 
					Toast.makeText(MainActivity.this, getString(R.string.double_click_exit), Toast.LENGTH_SHORT).show();   
	                firstTime = secondTime;//更新firstTime  
	                return true;   
	             } else {                                                    
	            	 //两次按键小于2秒时，退出应用  
	            	 MainActivity.this.finish();
	             }   
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void showBannerAD() {
		this.bannerAD = new AdView(this, AdSize.BANNER, Constants.APPId, Constants.BannerPosId);
		this.bannerAD.setAdListener(new AdListener() {
          
          @Override
          public void onNoAd() {
            Log.i("admsg:","Banner AD LoadFail");
          }
          
          @Override
          public void onBannerClosed() {
            //仅在开启广点通广告关闭按钮时生效
            Log.i("admsg:","Banner AD Closed");
          }
          
          @Override
          public void onAdReceiv() {
            Log.i("admsg:","Banner AD Ready to show");
          }
          
          @Override
          public void onAdExposure() {
            Log.i("admsg:","Banner AD Exposured");
          }
          
          @Override
          public void onAdClicked() {
          //Banner广告发生点击时回调，由于点击去重等因素不能保证回调数量与联盟最终统计数量一致
            Log.i("admsg:","Banner AD Clicked");
          }
        });
		this.bannerContainer.removeAllViews();
		this.bannerContainer.addView(bannerAD);
		AdRequest adr = new AdRequest();
		adr.setRefresh(31);
		bannerAD.fetchAd(adr);
	}
	
	private class MainAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<String> mList = new ArrayList<String>();

		public MainAdapter(Context context, ArrayList<String> list) {
			this.mContext = context;
			this.mList = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			String str = mList.get(arg0);
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.main_item, null);
			}
			Button btnName = (Button) view.findViewById(R.id.btn_name);
			btnName.setText(str);

			return view;
		}

	}
}
