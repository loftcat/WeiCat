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
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.Utility;
import com.weibo.sdk.android.WeiboException;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class SettingSetbackgroundPreviewAty extends BaseActivity {
	ImageView runImage;
	ImageView back;
	Button select;
	TranslateAnimation left, right;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

	@Override
	public void initView() {
		context = this;

		setContentView(R.layout.setting_setbackground_preview_aty);

		runImage = (ImageView) findViewById(R.id.settting_setbackground_preview_image);
		runImage.setBackgroundResource(getIntent().getIntExtra("image",
				R.drawable.background_1));
		back = (ImageView) findViewById(R.id.setting_setbackground_preview_back);
		select = (Button) findViewById(R.id.setting_setbackgroung_preview_ok);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utility.saveBackgroundId(getIntent().getIntExtra("imageId", 0),
						SettingSetbackgroundPreviewAty.this);
				finish();
			}
		});

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
