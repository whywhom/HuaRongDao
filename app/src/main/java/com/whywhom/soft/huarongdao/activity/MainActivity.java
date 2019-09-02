package com.whywhom.soft.huarongdao.activity;

import java.util.ArrayList;

import com.whywhom.soft.huarongdao.R;
import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.whywhom.soft.huarongdao.AppContext;
import com.whywhom.soft.huarongdao.util.Constants;

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
		initGDT();
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
	private void initGDT() {
		// TODO Auto-generated method stub
		// 打开debug开关，可查看mta上报日志或错误
		// 发布时，请务必要删除本行或设为false
		StatConfig.setDebugEnable(false);

		// 获取MTA MID等信息
//		logger.d(StatConfig.getDeviceInfo(this).toString());
		// 用户自定义UserId
		// StatConfig.setCustomUserId(this, "1234");
		java.util.UUID.randomUUID();
		// androidManifest.xml指定本activity最先启动，因此，MTA的初始化工作需要在onCreate中进行
		// 为了使得MTA配置及时生效，请确保MTA配置在调用StatService之前已被调用。
		// 推荐是在Activity.onCreate处初始化MTA设置
		// 根据不同的模式：调试或发布，初始化MTA设置
		initMTAConfig(false);

		/**
		 * 调用MTA一般需要3步：
		 * 1：配置manifest.xml权限
		 * 2：调用StatConfig相关的配置接口配置MTA
		 * 3:调用StatService相关的接口，开始统计！
		 */

		// StatCommonHelper.getLogger().setLogLevel(Log.VERBOSE);
		// 初始化并启动MTA
		// 第三方SDK必须按以下代码初始化MTA，其中appkey为规定的格式!!!
		// 其它普通的app可自行选择是否调用
		// try {
		// // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
		// // 用于MTA SDK版本冲突检测
		// StatService.startStatService(this, "A6TPGR6K3V94",
		// com.tencent.stat.common.StatConstants.VERSION);
		// } catch (com.tencent.stat.MtaSDkException e) {
		// // MTA初始化失败
		// logger.error("MTA start failed.");
		// }

		// // 获取在线参数
		// String onlineValue = StatConfig.getCustomProperty("onlineKey");
		// if(onlineValue.equalsIgnoreCase("on")){
		// // do something
		// }else{
		// // do something
		// }
	}
	/**
	 * 根据不同的模式，建议设置的开关状态，可根据实际情况调整，仅供参考。
	 * 
	 * @param isDebugMode
	 *            根据调试或发布条件，配置对应的MTA配置
	 */
	private void initMTAConfig(boolean isDebugMode) {
		if (isDebugMode) { // 调试时建议设置的开关状态
			// 查看MTA日志及上报数据内容
			StatConfig.setDebugEnable(true);
			// 禁用MTA对app未处理异常的捕获，方便开发者调试时，及时获知详细错误信息。
			// StatConfig.setAutoExceptionCaught(false);
			// StatConfig.setEnableSmartReporting(false);
			// Thread.setDefaultUncaughtExceptionHandler(new
			// UncaughtExceptionHandler() {
			//
			// @Override
			// public void uncaughtException(Thread thread, Throwable ex) {
			// logger.error("setDefaultUncaughtExceptionHandler");
			// }
			// });
			// 调试时，使用实时发送
			StatConfig.setStatSendStrategy(StatReportStrategy.INSTANT);
		} else { // 发布时，建议设置的开关状态，请确保以下开关是否设置合理
			// 禁止MTA打印日志
			StatConfig.setDebugEnable(false);
			// 根据情况，决定是否开启MTA对app未处理异常的捕获
			StatConfig.setAutoExceptionCaught(true);
			// 选择默认的上报策略
			StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);

		}
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
		// 如果本Activity是继承基类BaseActivity的，可注释掉此行。
		StatService.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.showBannerAD();
		// 如果本Activity是继承基类BaseActivity的，可注释掉此行。
		StatService.onResume(this);
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
