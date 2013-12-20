package com.loftcat.ui;

import java.io.IOException;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.adapter.BackgroundAdap;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.Utility;
import com.weibo.sdk.android.WeiboException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class SettingBackgroundAty extends BaseActivity {
	GridView gridView;

	BackgroundAdap backgroundAdap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backgroundAdap = new BackgroundAdap(this);
		gridView = (GridView) findViewById(R.id.setting_grid);
		gridView.setAdapter(backgroundAdap);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(
						AppConfig.INTENT_ACTION_BACKGROUND_PREVIEW);
				intent.putExtra("image",
						AppConfig.backgrounds[arg2].getLargeImage());
				intent.putExtra("imageId", arg2);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		backgroundAdap.notifyDataSetChanged();
		background.setImageResource(AppConfig.backgrounds[Utility
				.getBackgroundId(this)].getLargeImage());
	}

	@Override
	public void onComplete(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(WeiboException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIOException(IOException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	ImageView background;

	@Override
	public void initView() {
		setContentView(R.layout.setting_setbackground_aty);
		background = (ImageView) findViewById(R.id.settingbackground_background);
	}

	@Override
	public void initLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}
}
