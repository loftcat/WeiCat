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

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.adapter.AtFriendslistAdapter;
import com.loftcat.ui.adapter.FriendslistAdapter;
import com.loftcat.ui.utils.PullToRefreshView;
import com.loftcat.ui.utils.PullToRefreshView.OnFooterRefreshListener;
import com.loftcat.ui.utils.PullToRefreshView.OnHeaderRefreshListener;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.JSONHelper;
import com.loftcat.utils.Utility;
import com.loftcat.weibo.bean.UserVO;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.net.RequestListener;

public class AT extends BaseActivity implements OnHeaderRefreshListener,
		OnFooterRefreshListener {
	PullToRefreshView pullToRefreshView;
	private String mode;

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

	ListView listview;
	EditText editText;
	Button submit;
	ImageView at_background;

	@Override
	public void initView() {
		setContentView(R.layout.at);
		listview = (ListView) findViewById(R.id.at_listview);
		at_background = (ImageView) findViewById(R.id.at_background);
		editText = (EditText) findViewById(R.id.editText1);
		submit = (Button) findViewById(R.id.submit);
	}

	@Override
	protected void onResume() {
		super.onResume();
		at_background.setImageResource(AppConfig.backgrounds[Utility
				.getBackgroundId(this)].getLargeImage());
	}

	FriendshipsAPI friendshipsAPI;
	long id;
	int next_cursor = 0;
	int previous_cursor = 0;
	ArrayList<UserVO> userVOs = new ArrayList<UserVO>();

	@Override
	public void initLogic() {
		mode = getIntent().getStringExtra("mode");
		friendshipsAPI = new FriendshipsAPI(utility.getAccessToken());
		id = getIntent().getLongExtra("userID", 0);
		Log.d("RESULT", "id:" + id);
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
							Log.d("RESULT", "arg0:   " + arg0);
							JSONObject jsonObject = new JSONObject(arg0);
							next_cursor = jsonObject.getInt("next_cursor");
							previous_cursor = jsonObject
									.getInt("previous_cursor");
							JSONArray jsonarray = jsonObject
									.getJSONArray("users");
							userVOs.addAll(JSONHelper.parseCollection(
									jsonarray, ArrayList.class, UserVO.class));
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

	@Override
	public void initListener() {
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.at_pulltorefreshview);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterRefreshListener(this);
		listview.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (mode.equals("new")) {
					Intent intent = new Intent(AppConfig.INTENT_ACTION_SEND);
					intent.putExtra("mode", "new");
					intent.putExtra("id", id);
					intent.putExtra("at", userVOs.get(arg2).getName());
					startActivity(intent);
					finish();
				} else if (mode.equals("add")) {
					Intent intent = new Intent(AppConfig.INTENT_ACTION_SEND);
					intent.putExtra("at", userVOs.get(arg2).getName());
					setResult(RESULT_OK, intent);
					// setResult(int resultCode, Intent intent)
					finish();
				}
			}
		});
		submit.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = editText.getText().toString();

				if (msg != null && !msg.equals("")) {
					String trim = msg.trim();
					if (trim != null && !trim.equals("")) {
						if (mode.equals("new")) {
							Intent intent = new Intent(
									AppConfig.INTENT_ACTION_SEND);
							intent.putExtra("mode", "new");
							intent.putExtra("id", id);
							intent.putExtra("at", msg);
							startActivity(intent);
							finish();
						} else if (mode.equals("add")) {
							Intent intent = new Intent(
									AppConfig.INTENT_ACTION_SEND);
							intent.putExtra("at", msg);
							setResult(RESULT_OK, intent);
							// setResult(int resultCode, Intent intent)
							finish();
						}
					}
				}
			}
		});
	}

	PullToRefreshView puRefreshView;

	AtFriendslistAdapter friendslistAdapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.obj != null) {
				if (msg.what == 1) {
					friendslistAdapter = new AtFriendslistAdapter(AT.this,
							(ArrayList<UserVO>) msg.obj);
					// for (int i = 0; i < userVOs.size(); i++) {
					// LogCenter.getInstance().debug("RESULT",
					// userVOs.get(i).getProfile_image_url());
					// }
					listview.setAdapter(friendslistAdapter);
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
					listview.invalidate();
				}
			}

		}

	};

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		puRefreshView = view;

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

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		Log.d("RESULT", "onFooterRefresh");
		puRefreshView = view;

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
