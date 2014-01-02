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

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.Utility;
import com.umeng.fb.FeedbackAgent;
import com.weibo.sdk.android.WeiboException;

public class Setting extends BaseActivity {

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
		Intent intent = new Intent();

		switch (arg0.getId()) {
		case R.id.good:
			intent.setAction(AppConfig.INTENT_ACTION_GOOD);
			startActivity(intent);
			break;
		case R.id.version:
			intent.setAction(AppConfig.INTENT_ACTION_VERSION);
			startActivity(intent);
			break;
		case R.id.feedback:
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			break;
		case R.id.about:
			intent.setAction(AppConfig.INTENT_ACTION_ABOUT);
			startActivity(intent);
			break;
		case R.id.background:
			intent.setAction(AppConfig.INTENT_ACTION_BACKGROUND);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	RelativeLayout good;
	RelativeLayout version;
	RelativeLayout feedback;
	RelativeLayout about;
	RelativeLayout background;
	ImageView setting_background;

	@Override
	public void initView() {
		setContentView(R.layout.setting);
		good = (RelativeLayout) findViewById(R.id.good);
		version = (RelativeLayout) findViewById(R.id.version);
		feedback = (RelativeLayout) findViewById(R.id.feedback);
		about = (RelativeLayout) findViewById(R.id.about);
		background = (RelativeLayout) findViewById(R.id.background);
		setting_background = (ImageView) findViewById(R.id.setting_background);
		good.setOnClickListener(this);
		version.setOnClickListener(this);
		feedback.setOnClickListener(this);
		about.setOnClickListener(this);
		background.setOnClickListener(this);

	}

	@Override
	public void initLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		setting_background.setImageResource(AppConfig.backgrounds[Utility
				.getBackgroundId(this)].getLargeImage());
	}

}
