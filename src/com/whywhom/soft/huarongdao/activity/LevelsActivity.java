package com.whywhom.soft.huarongdao.activity;

import java.util.ArrayList;

import com.whywhom.soft.huarongdao.R;
import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.whywhom.soft.huarongdao.util.Constants;
import com.whywhom.soft.huarongdao.util.GameLevels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class LevelsActivity extends Activity {
	private GridView gv;
	private ArrayList<String> dataList = new ArrayList<String>();
	private GridViewAdapter adapter = null;
	private RelativeLayout bannerContainer;
	private AdView bannerAD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_levels);
		initData();
		this.bannerContainer = (RelativeLayout) this.findViewById(R.id.bannercontainer);
		gv = (GridView) findViewById(R.id.gv_gamelevel);
		adapter = new GridViewAdapter(this, dataList);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectLevel(position);
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
		this.showBannerAD();
	}
	private void selectLevel(int level) {
		Intent intent = new Intent();
		intent.setClass(LevelsActivity.this,
				GameActivity.class);
		intent.putExtra("level", level);
		startActivity(intent);
	}
	private void initData() {
		if(GameLevels.chessNameArray != null){
			for(int i=0; i<GameLevels.chessNameArray.length; i++){
				dataList.add(getString(GameLevels.chessNameArray[i]));
			}
		}
	}
	
	private class GridViewAdapter extends BaseAdapter {
		private Context mContext;

		private ArrayList<String> list;

		public GridViewAdapter(Context c,
				ArrayList<String> alist) {
			mContext = c;
			list = alist;
		}

		public int getCount() {
			if (list == null)
				return 0;
			else
				return list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final Holder holder;
			if (list == null || position >= list.size())
				return null;
			String str = list.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.level_item, null);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.btnName = (Button) convertView.findViewById(R.id.btn_name);
			holder.btnName.setText(str);				
			convertView.setTag(holder);
			return convertView;
		}

		class Holder {
			Button btnName;
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
