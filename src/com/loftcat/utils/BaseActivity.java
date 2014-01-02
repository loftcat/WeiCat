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
package com.loftcat.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.loftcat.R;
import com.loftcat.utils.DBManager;
import com.loftcat.weibo.bean.Account;
import com.umeng.analytics.MobclickAgent;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.AsyncWeiboRunner;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 基类
 * 
 * @author HeBin hisneric@gmail.com
 * 
 * @version 1.0
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		RequestListener, OnClickListener {
	protected static Account _account;

	public static Account get_account() {
		return _account;
	}

	public static void set_account(Account _account) {
		BaseActivity._account = _account;
	}

	// ----------------------公用参数--------------------------------//
	private final String TAG = "BaseActivity";
	public static Boolean isProtected = false;
	public static Boolean isSina = false;// 标识符
	public DBManager mDBManager;
	public String token = "";// 这里token即代表Sina中的token，也代表Tencent中的AccessToken
	public String expires_in = "";
	public String plf = "";
	public final String WHOAMI = "who_am_i";

	// ----------------------新浪API接口所需的参数-----------------------------//
	public static final String TOKEN = "com.logan.weibo.token";
	public static final String EXPIRE = "com.logan.weibo.expire";
	// public Weibo mWeibo = Weibo.getInstance();// Sina API 实例
	public static final String CONSUMER_KEY = "2313285874";
	public static final String CONSUMER_SECRET = "fd67f1e50e4c274b02890128d8d77c93";
	// 此处回调页内容应该替换为与appkey对应的应用回调页
	// 应用回调页不可为空
	public static final String REDIRECT_URL = "https://github.com/loftcat/WeiCat";

	protected static Utility utility;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDBManager = new DBManager(getApplicationContext());
		initView();
		initLogic();
		initListener();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
	}

	public abstract void initView();

	public abstract void initLogic();

	public abstract void initListener();


}
