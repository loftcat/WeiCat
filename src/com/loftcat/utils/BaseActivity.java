/**   
 * Copyright (c) 2013 by Logan.	
 *   
 * 爱分享-微博客户端，是一款运行在android手机上的开源应用，代码和文档已托管在GitHub上，欢迎爱好者加入
 * 1.授权认证：Oauth2.0认证流程
 * 2.服务器访问操作流程
 * 3.新浪微博SDK和腾讯微博SDK
 * 4.HMAC加密算法
 * 5.SQLite数据库相关操作
 * 6.字符串处理，表情识别
 * 7.JSON解析，XML解析：超链接解析，时间解析等
 * 8.Android UI：样式文件，布局
 * 9.异步加载图片，异步处理数据，多线程  
 * 10.第三方开源框架和插件
 *    
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
 * @author Logan <a href="https://github.com/Logan676/JustSharePro"/>
 * 
 * @version 1.0
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		RequestListener, OnClickListener, OnGesturePerformedListener {
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
	public static final String REDIRECT_URL = "https://github.com/loftcat/NoteCat";

	protected static Utility utility;
	GestureLibrary mLibrary;

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

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

		// We want at least one prediction
		if (predictions.size() > 0) {
			Prediction prediction = (Prediction) predictions.get(0);
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				// Show the spell
				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

}
