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
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.app.AppContext;
import com.loftcat.utils.cache.CacheManager;
import com.loftcat.utils.database.DBManager;
import com.loftcat.utils.weibo.AccountManager;
import com.umeng.update.UmengUpdateAgent;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class LoadingAty extends BaseActivity {

	private Weibo mWeibo;
	private static final String CONSUMER_KEY = "2313285874";// 替换为开发者的appkey，例如"1646212860";
	private static final String CONSUMER_SECRET = "fd67f1e50e4c274b02890128d8d77c93";// 替换为开发者的appkey，例如"1646212860";
	private static final String REDIRECT_URL = "https://github.com/loftcat/NoteCat";
	public static final String TAG = "sinasdk";
	boolean isRefresh = false;

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

	ImageView background;

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		background = (ImageView) findViewById(R.id.loading_background);
		int selectedId = CacheManager.getBackGroundCache().getBackgroundId(this);
		background.setImageResource(selectedId);

	}

	@Override
	public void initLogic() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		AppContext.WINDOW_WIDTH = dm.widthPixels;
		AppContext.WINDOW_HEIGHT = dm.heightPixels;
		utility = new AccountManager(mDBManager, this);
		if (mDBManager.getAccounts() != null
				&& mDBManager.getAccounts().size() > 0) {
			Long expires_in = Long.valueOf(mDBManager.getAccounts().get(0)
					.getExpires_in());
			Long current_time = System.currentTimeMillis();
			
//			Log.e("error", "expires_in:" + expires_in);
//			Log.e("error", "current_time:" + current_time);
//			Log.e("error", "expires_in-current_time:"
//					+ (expires_in - current_time));

			if (current_time < expires_in - 86400000) {
				
				Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken(
						mDBManager.getAccounts().get(0).getToken(), mDBManager
								.getAccounts().get(0).getExpires_in());
				oauth2AccessToken.setExpiresIn(String.valueOf(expires_in));
				utility.setAccessToken(oauth2AccessToken);
				
				Intent intent = new Intent(AppConfig.INTENT_ACTION_HOMEPAGE);
				intent.putExtra("index", utility.readIndex());
				startActivity(intent);
				finish();
				
			} else {
				
				isRefresh = true;
				mWeibo = Weibo.getInstance(CONSUMER_KEY, CONSUMER_SECRET,
						REDIRECT_URL);
				mWeibo.authorize(LoadingAty.this, new AuthDialogListener());
				
			}

		} else {
			mWeibo = Weibo.getInstance(CONSUMER_KEY, CONSUMER_SECRET,
					REDIRECT_URL);
			mWeibo.authorize(LoadingAty.this, new AuthDialogListener());
		}
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken(token,
					expires_in);
			// oauth2AccessToken.setExpiresTime(Long.valueOf(expires_in));
			utility.setAccessToken(oauth2AccessToken);
			// String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
			// .format(new java.util.Date(accessToken.getExpiresTime()));
			// mText.setText("认证成功: \r\n access_token: " + token + "\r\n"
			// + "expires_in: " + expires_in + "\r\n有效期：" + date);
			// Account account = new Account(name, id, url, plf, token,
			// expires_in);
			saveAccount();
			// Log.d("RESULT", "认证成功: \r\n access_token: " + token + "\r\n"
			// + "expires_in: " + expires_in + "\r\n有效期：" + date);
			// try {
			// Class sso = Class
			// .forName("com.weibo.sdk.android.api.WeiboAPI");//
			// 如果支持weiboapi的话，显示api功能演示入口按钮
			// } catch (ClassNotFoundException e) {
			// // e.printStackTrace();
			// Log.d("RESULT",
			// "com.weibo.sdk.android.api.WeiboAPI not found");
			// }
			// AccessTokenKeeper.keepAccessToken(Loading.this,
			// accessToken);

			// Toast.makeText(Loading.this, "认证成功",
			// Toast.LENGTH_SHORT).show();

		}

		ProgressDialog progressDialog;

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
			new AlertDialog.Builder(LoadingAty.this)
					.setTitle("Warning!")
					.setMessage(
							"Please turn on wifi or gprs, this app is just work on wifi environment,Thank you.")
					.setPositiveButton("ok", new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).create().show();
			

		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	private String userName = "";
	private String id = "";

	private Boolean saveAccount() {
		try {
			utility.getUID(utility.getAccessToken(), handler, isRefresh);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == -1) {
				Toast.makeText(LoadingAty.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
			} else if (msg.what == 1) {
				Toast.makeText(LoadingAty.this, "认证成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(AppConfig.INTENT_ACTION_HOMEPAGE);
				startActivity(intent);
				finish();
			}

			super.handleMessage(msg);
		}

	};
}
