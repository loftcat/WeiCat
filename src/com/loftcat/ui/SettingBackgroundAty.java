/*
 * Copyright (c) 2013 HeBin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.loftcat.ui;

import java.io.IOException;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.adapter.BackgroundAdap;
import com.loftcat.utils.cache.BackGroundCache;
import com.loftcat.utils.cache.CacheManager;
import com.loftcat.utils.weibo.AccountManager;
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
						CacheManager.backgrounds[arg2].getLargeImage());
				intent.putExtra("imageId", arg2);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		backgroundAdap.notifyDataSetChanged();
		background.setImageResource(CacheManager.backgrounds[CacheManager.getBackGroundCache()
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
