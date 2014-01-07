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
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.adapter.FriendslistAdapter;
import com.loftcat.ui.utils.PullToRefreshView;
import com.loftcat.ui.utils.PullToRefreshView.OnFooterRefreshListener;
import com.loftcat.ui.utils.PullToRefreshView.OnHeaderRefreshListener;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.LogCenter;
import com.loftcat.utils.Utility;
import com.loftcat.weibo.sdk.FriendshipsAPI;
import com.loftcat.weibo.sdk.WeiboAPI.FEATURE;
import com.loftcat.weibo.vo.CommentVo;
import com.loftcat.weibo.vo.StatusVo;
import com.loftcat.weibo.vo.UserVO;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

@SuppressLint("NewApi")
public class FriendsListAty extends BaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	PullToRefreshView pullToRefreshView;

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
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		setContentView(R.layout.friendslist);
		friendsListView = (ListView) findViewById(R.id.friendslist_listview);
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.friends_pulltorefreshview);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterRefreshListener(this);
	}

	FriendshipsAPI friendshipsAPI;
	FriendslistAdapter friendslistAdapter;
	ListView friendsListView;
	int count = 1;
	private int next_cursor = 0;
	private int previous_cursor = 0;
	ArrayList<UserVO> userVOs = new ArrayList<UserVO>();
	private String mode = "";
	private String id = "";
	private Gson gson;
	@Override
	public void initLogic() {
		mode = getIntent().getStringExtra("mode");
		id = getIntent().getStringExtra("id");
		gson =new Gson();
		friendshipsAPI = new FriendshipsAPI(utility.getAccessToken());
		if (mode.equals("fans")) {
			friendshipsAPI.followers(Long.valueOf(id), 20, next_cursor, true,
					new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub

						}

						@SuppressWarnings("unchecked")
						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								next_cursor = jsonObject.getInt("next_cursor");
								previous_cursor = jsonObject
										.getInt("previous_cursor");
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								
								userVOs.addAll((ArrayList<UserVO>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<UserVO>>(){}.getType()));

								Message msg = new Message();
								msg.what = 1;
								msg.obj = userVOs;
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					});
		} else if (mode.equals("focus")) {

			friendshipsAPI.friends(Long.valueOf(id), 20, next_cursor, true,
					new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub

						}

						@SuppressWarnings("unchecked")
						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								next_cursor = jsonObject.getInt("next_cursor");
								previous_cursor = jsonObject
										.getInt("previous_cursor");
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								userVOs.addAll((ArrayList<UserVO>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<UserVO>>(){}.getType()));
								Message msg = new Message();
								msg.what = 1;
								msg.obj = userVOs;
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					});
		}

	}

	@Override
	public void initListener() {
		friendsListView
				.setOnItemClickListener(new ListView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent();
						intent.putExtra("userID",
								String.valueOf(userVOs.get(arg2).getId()));
						intent.setAction(AppConfig.INTENT_ACTION_SELFPAGE);
						startActivity(intent);
					}
				});
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.obj != null) {
				if (msg.what == 1) {
					friendslistAdapter = new FriendslistAdapter(
							FriendsListAty.this, (ArrayList<UserVO>) msg.obj);
					// for (int i = 0; i < userVOs.size(); i++) {
					// LogCenter.getInstance().debug("RESULT",
					// userVOs.get(i).getProfile_image_url());
					// }
					friendsListView.setAdapter(friendslistAdapter);
				} else if (msg.what == 3) {
					userVOs.addAll((ArrayList<UserVO>) msg.obj);
					friendslistAdapter.notifyDataSetChanged();
					puRefreshView.onFooterRefreshComplete();
				} else if (msg.what == 2) {
					ArrayList<UserVO> cache = new ArrayList<UserVO>();
					cache.addAll((Collection<? extends UserVO>) msg.obj);
					cache.addAll(userVOs);
					userVOs = cache;
					friendslistAdapter.notifyDataSetChanged();
					puRefreshView.onHeaderRefreshComplete();
					friendslistAdapter.reSetData(userVOs);
					friendsListView.invalidate();
				}
			}

		}

	};
	PullToRefreshView puRefreshView;

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		Log.d("RESULT", "onFooterRefresh");
		puRefreshView = view;
		if (mode.equals("fans")) {
			friendshipsAPI.followers(Long.valueOf(id), 20, next_cursor, true,
					new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub

						}

						@SuppressWarnings("unchecked")
						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								next_cursor = jsonObject.getInt("next_cursor");
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								Message msg = new Message();
								msg.what = 3;
//								msg.obj = JSONHelper.parseCollection(jsonarray,
//										ArrayList.class, UserVO.class);
								msg.obj =(ArrayList<UserVO>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<UserVO>>(){}.getType());

								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		} else if (mode.equals("focus")) {
			friendshipsAPI.friends(Long.valueOf(id), 20, next_cursor, true,
					new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub

						}

						@SuppressWarnings("unchecked")
						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								next_cursor = jsonObject.getInt("next_cursor");
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								Message msg = new Message();
								msg.what = 3;
								msg.obj =(ArrayList<UserVO>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<UserVO>>(){}.getType());
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		}

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		puRefreshView = view;
		if (mode.equals("fans")) {
			friendshipsAPI.followers(Long.valueOf(id), 20, previous_cursor,
					true, new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub

						}

						@SuppressWarnings("unchecked")
						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								Message msg = new Message();
								msg.what = 2;
								msg.obj =(ArrayList<UserVO>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<UserVO>>(){}.getType());
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		} else if (mode.equals("focus")) {
			friendshipsAPI.friends(Long.valueOf(id), 20, previous_cursor, true,
					new RequestListener() {

						@Override
						public void onIOException(IOException arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(WeiboException arg0) {
							// TODO Auto-generated method stub

						}

						@SuppressWarnings("unchecked")
						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								Message msg = new Message();
								msg.what = 2;
								msg.obj =(ArrayList<UserVO>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<UserVO>>(){}.getType());
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		}

	}

}
