package com.android.why.huarongdao.activity;

import java.util.ArrayList;

import com.android.why.huarongdao.AppContext;
import com.android.why.huarongdao.R;
import com.android.why.huarongdao.util.Constants;
import com.android.why.huarongdao.util.GameLevels;
import com.android.why.huarongdao.util.ScoreItem;
import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RankActivity extends Activity{
	private TextView tv;
	private ListView lv;
	private RankAdapter adapter = null;
	private ArrayList<ScoreItem> dataList = new ArrayList<ScoreItem>();
	private int level = 0;
	private AdView bannerAD;
	private RelativeLayout bannerContainer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		this.bannerContainer = (RelativeLayout) this.findViewById(R.id.bannercontainer);
		this.setTitle(getString(R.string.bt_rank)); 
		Intent intent = getIntent();
		if(intent != null){
			level = intent.getIntExtra("level",0);
			dataList = AppContext.dbHelper.getItems(getString(GameLevels.chessNameArray[level]));
		}
		tv = (TextView) findViewById(R.id.tv);
		if(dataList.size()>0){
			tv.setText(dataList.get(0).level);
		}
		lv = (ListView) findViewById(R.id.lv_rank);
		adapter = new RankAdapter(this, dataList);
		lv.setAdapter(adapter);
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
		this.showBannerAD();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			//点后退键的时候,为了防止点得过快,触发两次后退事件,故做此设置.
			if(event.getRepeatCount() == 0){
	           RankActivity.this.finish(); 
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	private class RankAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<ScoreItem> mList = new ArrayList<ScoreItem>();

		public RankAdapter(Context context, ArrayList<ScoreItem> list) {
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
			ScoreItem si = mList.get(arg0);
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.rank_item, null);
			}
			TextView tvlevel = (TextView) view.findViewById(R.id.tv_level);
			TextView tvscore = (TextView) view.findViewById(R.id.tv_score);
			TextView tvplayer = (TextView) view.findViewById(R.id.tv_player);
			tvlevel.setText("布局："+si.level);
			tvscore.setText("成绩："+String.valueOf(si.score));
			tvplayer.setText("玩家："+si.name);
			return view;
		}

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
}
