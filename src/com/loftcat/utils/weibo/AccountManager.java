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
package com.loftcat.utils.weibo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loftcat.app.AppConfig;
import com.loftcat.utils.database.DBManager;
import com.loftcat.utils.log.LogCenter;
import com.loftcat.weibo.sdk.AccountAPI;
import com.loftcat.weibo.sdk.UsersAPI;
import com.loftcat.weibo.vo.AccountVo;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class AccountManager {
	public AccountManager(Context context) {
		this.context = context;
	}

	private Context context;
	private DBManager dmManager;

	public DBManager getDmManager() {
		return dmManager;
	}

	public void setDmManager(DBManager dmManager) {
		this.dmManager = dmManager;
	}

	public Oauth2AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(Oauth2AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public AccountManager(DBManager dmManager, Context context) {
		this.dmManager = dmManager;
		this.context = context;
	}

	public Oauth2AccessToken accessToken;

	String uid = "";
	String name = "";
	Long uid_long = 0l;
	String userInfo = "";



	/**
	 * Sina 获取登录用户的ID
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws WeiboException
	 */
	public void getUID(Oauth2AccessToken accessToken, final Handler handler,
			final boolean isRefresh) throws MalformedURLException, IOException,
			WeiboException {

		AccountAPI accountAPI = new AccountAPI(accessToken);

		accountAPI.getUid(new RequestListener() {

			@Override
			public void onIOException(IOException arg0) {
				Log.d("RESULT", "onIOException:  " + arg0);

			}

			@Override
			public void onError(WeiboException arg0) {
				Log.d("RESULT", "onError:  " + arg0);
			}

			@Override
			public void onComplete(String arg0) {
				Log.d("RESULT", "onComplete:  " + arg0);
				try {
					JSONObject jsonObject = new JSONObject(arg0);
					uid_long = jsonObject.getLong("uid");
					uid = String.valueOf(uid_long);
					try {
						getUserInfo(uid_long, handler, isRefresh);
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
				} catch (JSONException e) {
					Message msg = new Message();
					msg.what = -1;
					msg.obj = e.toString();
					handler.sendMessage(msg);
				}
			}
		});
	}

	/**
	 * Sina 根据用户ID获取用户信息
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws WeiboException
	 */
	public String getUserInfo(Long uid_long, final Handler handler,
			final boolean isRefresh) throws MalformedURLException, IOException,
			WeiboException {

		UsersAPI usersAPI = new UsersAPI(accessToken);
		usersAPI.show(uid_long, new RequestListener() {

			@Override
			public void onIOException(IOException arg0) {
				Log.d("RESULT", "IOException--" + arg0);
			}

			@Override
			public void onError(WeiboException arg0) {
				Log.d("RESULT", "onError--" + arg0);

			}

			@Override
			public void onComplete(String arg0) {
				try {
					userInfo = arg0;
					JSONObject jsonObject = new JSONObject(arg0);
					name = jsonObject.getString("name");
					addAccount(handler, isRefresh);
				} catch (JSONException e) {
					Message msg = new Message();
					msg.what = -1;
					msg.obj = e.toString();
					handler.sendMessage(msg);
				}

				// Log.d("RESULT", "onComplete--" + name);
			}
		});
		return name;
	}
	
//    public AccountVo(Long id, String uid, String userInfo, String token, String expires_in, String name) {

	public void addAccount(Handler handler, boolean isRefresh) {
		if (!isRefresh) {
			LogCenter.getInstance().error("error", "addAccount--");
			dmManager.addAccount(new AccountVo(null,uid,	userInfo, accessToken
					.getToken(), String.valueOf(accessToken.getExpiresTime()),
					name));
		} else {
			dmManager.updateExpires_in(new AccountVo(null,uid,	userInfo, accessToken
					.getToken(), String.valueOf(accessToken.getExpiresTime()),
					name));
		}

		keepIndex(Long.valueOf(uid), handler);
	}

	public void keepIndex(long index, Handler handler) {
		SharedPreferences pref = context.getSharedPreferences(
				AppConfig.PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putLong("index", index);
		editor.commit();
		Message msg = new Message();
		msg.what = 1;
		msg.obj = "ok";
		handler.sendMessage(msg);
	}

	public void keepIndex(long index) {
		SharedPreferences pref = context.getSharedPreferences(
				AppConfig.PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putLong("index", index);
		editor.commit();
	}

	public long readIndex() {
		SharedPreferences pref = context.getSharedPreferences(
				AppConfig.PREFERENCES_NAME, Context.MODE_APPEND);
		return pref.getLong("index", 0);
	}

	public void loadUserData(final Oauth2AccessToken accessToken,
			final Handler handler, final boolean isRefresh) {
		AccountAPI accountAPI = new AccountAPI(accessToken);

		accountAPI.getUid(new RequestListener() {

			@Override
			public void onIOException(IOException arg0) {
				Log.d("RESULT", "onIOException:  " + arg0);

			}

			@Override
			public void onError(WeiboException arg0) {
				Log.d("RESULT", "onError:  " + arg0);
			}

			@Override
			public void onComplete(String arg0) {
				Log.d("RESULT", "onComplete:  " + arg0);

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(arg0);
					uid_long = jsonObject.getLong("uid");
					uid = String.valueOf(uid_long);

					for (int i = 0; i < dmManager.getUids().size(); i++) {
						if (uid.equals(dmManager.getUids().get(i))) {
							return;
						}
					}

					UsersAPI usersAPI = new UsersAPI(accessToken);
					usersAPI.show(uid_long, new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							Log.d("RESULT", "IOException--" + arg0);
						}

						@Override
						public void onError(WeiboException arg0) {
							Log.d("RESULT", "onError--" + arg0);

						}

						@Override
						public void onComplete(String arg0) {
							try {
								userInfo = arg0;
								JSONObject jsonObject = new JSONObject(arg0);
								name = jsonObject.getString("name");
								addAccount(handler, isRefresh);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	}


}
