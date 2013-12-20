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

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.adapter.FriendslistAdapter;
import com.loftcat.ui.utils.PullToRefreshView;
import com.loftcat.ui.utils.PullToRefreshView.OnFooterRefreshListener;
import com.loftcat.ui.utils.PullToRefreshView.OnHeaderRefreshListener;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.JSONHelper;
import com.loftcat.utils.LogCenter;
import com.loftcat.utils.Utility;
import com.loftcat.weibo.bean.StatusVo;
import com.loftcat.weibo.bean.UserVO;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
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

	@Override
	public void initLogic() {
		mode = getIntent().getStringExtra("mode");
		id = getIntent().getStringExtra("id");
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

						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								next_cursor = jsonObject.getInt("next_cursor");
								previous_cursor = jsonObject
										.getInt("previous_cursor");
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								userVOs.addAll(JSONHelper.parseCollection(
										jsonarray, ArrayList.class,
										UserVO.class));
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

						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								next_cursor = jsonObject.getInt("next_cursor");
								previous_cursor = jsonObject
										.getInt("previous_cursor");
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								userVOs.addAll(JSONHelper.parseCollection(
										jsonarray, ArrayList.class,
										UserVO.class));
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

						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								next_cursor = jsonObject.getInt("next_cursor");
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								Message msg = new Message();
								msg.what = 3;
								msg.obj = JSONHelper.parseCollection(jsonarray,
										ArrayList.class, UserVO.class);
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

						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								next_cursor = jsonObject.getInt("next_cursor");
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								Message msg = new Message();
								msg.what = 3;
								msg.obj = JSONHelper.parseCollection(jsonarray,
										ArrayList.class, UserVO.class);
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

						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								Message msg = new Message();
								msg.what = 2;
								msg.obj = JSONHelper.parseCollection(jsonarray,
										ArrayList.class, UserVO.class);
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

						@Override
						public void onComplete(String arg0) {
							try {
								JSONObject jsonObject = new JSONObject(arg0);
								JSONArray jsonarray = jsonObject
										.getJSONArray("users");
								Message msg = new Message();
								msg.what = 2;
								msg.obj = JSONHelper.parseCollection(jsonarray,
										ArrayList.class, UserVO.class);
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
