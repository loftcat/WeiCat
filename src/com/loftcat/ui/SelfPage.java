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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.adapter.ExpandableAdapter;
import com.loftcat.ui.adapter.StatusExpandableAdapter;
import com.loftcat.ui.utils.PullToRefreshView;
import com.loftcat.ui.utils.PullToRefreshView.OnFooterRefreshListener;
import com.loftcat.ui.utils.PullToRefreshView.OnHeaderRefreshListener;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.ImageUtils;
import com.loftcat.utils.JSONHelper;
import com.loftcat.utils.Utility;
import com.loftcat.weibo.bean.FriendShipVo;
import com.loftcat.weibo.bean.StatusVo;
import com.loftcat.weibo.bean.UserVO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class SelfPage extends BaseActivity implements OnHeaderRefreshListener,
		OnFooterRefreshListener {
	private DisplayImageOptions options;

	protected ImageLoader imageLoader = ImageLoader.getInstance();

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

	TextView msg;
	UserVO userVo;
	ImageView headImage;
	TextView name;
	TextView location;
	TextView fans;
	TextView focus;
	TextView statues;
	ExpandableListView selfaty_listview;
	StatusesAPI statusesAPI;
	PullToRefreshView pullToRefreshView;
	RelativeLayout layout;
	LinearLayout fansLayout;
	LinearLayout focusLayout;
	TextView job;
	TextView introduce_title;
	TextView introduce;
	TextView blog_title;
	TextView blog;
	ImageView vip;
	TextView selfaty_relationship;
	TextView selfaty_atta;
	LinearLayout selfaty_relationship_layout;
	ImageView male;
	ProgressBar progressBar;

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		setContentView(R.layout.selfaty);
		progressBar = (ProgressBar) findViewById(R.id.selfaty_progress);
		headImage = (ImageView) findViewById(R.id.selfaty_head);
		name = (TextView) findViewById(R.id.selfaty_name_textview);
		male = (ImageView) findViewById(R.id.selfaty_male);
		location = (TextView) findViewById(R.id.selfaty_name_location);
		fans = (TextView) findViewById(R.id.selfaty_fans_textview);
		focus = (TextView) findViewById(R.id.selfaty_focus_textview);
		statues = (TextView) findViewById(R.id.selfaty_statuses_textview);
		layout = (RelativeLayout) findViewById(R.id.selfaty_layout);
		selfaty_listview = (ExpandableListView) findViewById(R.id.selfaty_listview);
		fansLayout = (LinearLayout) findViewById(R.id.selfaty_fans_layout);
		focusLayout = (LinearLayout) findViewById(R.id.selfaty_focus_layout);
		vip = (ImageView) findViewById(R.id.selfaty_vip);
		job = (TextView) findViewById(R.id.selfaty_job);
		introduce_title = (TextView) findViewById(R.id.selfaty_introduce_title);
		introduce = (TextView) findViewById(R.id.selfaty_introduce);
		blog_title = (TextView) findViewById(R.id.selfaty_blog_title);
		blog = (TextView) findViewById(R.id.selfaty_blog);
		selfaty_relationship = (TextView) findViewById(R.id.selfaty_relationship);
		selfaty_atta = (TextView) findViewById(R.id.selfaty_atta);
		selfaty_relationship_layout = (LinearLayout) findViewById(R.id.selfaty_relationship_layout);

		if (getIntent().getBooleanExtra("self", false)) {
			selfaty_relationship.setVisibility(View.GONE);
			selfaty_atta.setVisibility(View.GONE);
			selfaty_relationship_layout.setVisibility(View.GONE);
		}

		pullToRefreshView = (PullToRefreshView) findViewById(R.id.selfaty_pulltorefreshview);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterRefreshListener(this);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.head_default)
				.showImageForEmptyUri(R.drawable.head_default).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(0)).build();
		this.context = this;
	}

	String id;
	int count = 1;
	FriendshipsAPI friendshipsAPI;

	@Override
	public void initLogic() {
		id = getIntent().getStringExtra("userID");

		UsersAPI usrApi = new UsersAPI(utility.getAccessToken());

		usrApi.show(Long.parseLong(id), new RequestListener() {

			@Override
			public void onIOException(IOException arg0) {
				// TODO Auto-generated method stub
				Log.d("RESULT", "onIOException" + arg0.getMessage());

			}

			@Override
			public void onError(WeiboException arg0) {
				Log.d("RESULT", "onError" + arg0.getMessage());

			}

			@Override
			public void onComplete(final String arg0) {
				if (arg0 != null) {
					try {
						userVo = JSONHelper.parseObject(arg0, UserVO.class);
						if (userVo != null) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {

									imageLoader.displayImage(
											userVo.getAvatar_large(),
											headImage, options);
									name.setText(userVo.getName());
									location.setText(userVo.getLocation());
									fans.setText(String.valueOf(userVo
											.getFollowers_count()));
									focus.setText(String.valueOf(userVo
											.getFriends_count()));
									statues.setText(String.valueOf(userVo
											.getStatuses_count()));
									if (userVo.getGender().equals("m")) {
										Bitmap bitmap = ImageUtils.getRoundedCornerBitmap(
												BitmapFactory.decodeResource(
														getResources(),
														R.drawable.male),
												Utility.dip2px(SelfPage.this,
														10));
										male.setImageBitmap(bitmap);
									} else if (userVo.getGender().equals("f")) {
										Bitmap bitmap = ImageUtils.getRoundedCornerBitmap(
												BitmapFactory.decodeResource(
														getResources(),
														R.drawable.female),
												Utility.dip2px(SelfPage.this,
														10));
										male.setImageBitmap(bitmap);
									} else {
										Bitmap bitmap = ImageUtils.getRoundedCornerBitmap(
												BitmapFactory.decodeResource(
														getResources(),
														R.drawable.haha), 5);
										male.setImageBitmap(bitmap);
									}

									if (userVo.isVerified()) {
										vip.setImageResource(R.drawable.v);
										job.setText(userVo.getVerified_reason() == null ? ""
												: userVo.getVerified_reason());
									} else {
										vip.setVisibility(View.GONE);
										job.setVisibility(View.GONE);
									}
									if (userVo.getDescription() != null
											&& !userVo.getDescription().equals(
													"")) {
										introduce_title.setText("简介");
										introduce.setText(userVo
												.getDescription());
									} else {
										introduce_title
												.setVisibility(View.GONE);
										introduce.setVisibility(View.GONE);
									}

									if (userVo.getUrl() != null
											&& !userVo.getUrl().equals("")) {
										blog_title.setText("博客");
										blog.setText(userVo.getUrl());
									} else {
										blog_title.setVisibility(View.GONE);
										blog.setVisibility(View.GONE);
									}

								}
							});
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					count_progress++;
					if (count_progress == 3) {
						runOnUiThread(new Runnable() {
							public void run() {
								progressBar.setVisibility(View.INVISIBLE);

							}
						});
					}
				}

			}
		});
		statusesAPI = new StatusesAPI(utility.getAccessToken());
		statusesAPI.userTimeline(Long.parseLong(id), 0, 0, 20, count++, false,
				FEATURE.ALL, false, new RequestListener() {

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
						Message message = new Message();
						message.obj = arg0;
						message.what = 1;
						hanlder.sendMessage(message);
					}
				});

		friendshipsAPI = new FriendshipsAPI(utility.getAccessToken());

		friendshipsAPI.show(Long.parseLong(_account.getId()),
				Long.parseLong(id), new RequestListener() {

					@Override
					public void onIOException(IOException arg0) {
						Log.d("RESULT", arg0.toString() + "friendShip");
					}

					@Override
					public void onError(WeiboException arg0) {
						Log.d("RESULT", arg0.toString() + "friendShip");
					}

					@Override
					public void onComplete(String arg0) {

						try {
							JSONObject jsonObject = new JSONObject(arg0);
							JSONObject friendShip = jsonObject
									.getJSONObject("source");
							final FriendShipVo friendShipVo = JSONHelper
									.parseObject(friendShip.toString(),
											FriendShipVo.class);
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if ((friendShipVo.isFollowed_by() && friendShipVo
											.isFollowing())
											|| (!friendShipVo.isFollowed_by() && friendShipVo
													.isFollowing())) {
										selfaty_relationship.setText("取消关注");
										selfaty_relationship
												.setBackgroundColor(getResources()
														.getColor(
																R.color.button_green));
										selfaty_atta.setText("@TA");
										selfaty_atta
												.setBackgroundResource(R.drawable.button);
										selfaty_relationship
												.setOnClickListener(new TextView.OnClickListener() {

													@Override
													public void onClick(View v) {
														release();
													}
												});
									} else if (friendShipVo.isFollowed_by()
											&& !friendShipVo.isFollowing()) {
										selfaty_relationship.setText("关注TA");
										selfaty_relationship
												.setBackgroundColor(Color.rgb(
														255, 150, 0));
										selfaty_atta.setText("@TA");
										selfaty_relationship
												.setOnClickListener(new TextView.OnClickListener() {

													@Override
													public void onClick(View v) {
														focus();
													}
												});
									} else if (_account.getId().equals(id)) {
										selfaty_relationship_layout
												.setVisibility(View.GONE);
									} else {
										selfaty_relationship.setText("关注TA");
										selfaty_relationship
												.setBackgroundColor(Color.rgb(
														255, 150, 0));
										selfaty_atta.setText("@TA");
										selfaty_relationship
												.setOnClickListener(new TextView.OnClickListener() {

													@Override
													public void onClick(View v) {
														focus();
													}
												});
									}
								}
							});

						} catch (JSONException e) {
							Log.d("RESULT", e.toString() + "friendShip");
						}
						count_progress++;
						if (count_progress == 3) {
							progressBar.setVisibility(View.INVISIBLE);
						}
					}
				});

	}

	@Override
	public void initListener() {
		fansLayout.setOnClickListener(this);
		focusLayout.setOnClickListener(this);

		Log.d("RESULT", "xxxonclick");
		selfaty_listview.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView parent,
					View clickedView, int groupPosition, long groupId) {
				return false;
			}
		});
		selfaty_listview.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView expandablelistview,
					View clickedView, int groupPosition, int childPosition,
					long childId) {
				Log.d("RESULT", "child_click");
				return false;// 返回true表示此事件在此被处理了
			}
		});
		selfaty_listview
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					public void onGroupCollapse(int groupPosition) {
					}
				});
		selfaty_listview.setOnGroupExpandListener(new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < statusVos.size(); i++) {
					if (groupPosition != i) {
						selfaty_listview.collapseGroup(i);

					}
				}
			}
		});
		selfaty_listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		selfaty_listview.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// if (totalItemCount != 0
				// && !_isFresh
				// && firstVisibleItem + visibleItemCount >= totalItemCount) {
				// if (totalItemCount < Integer.valueOf(_status_count)) {
				// showDialog(DIALOG_LOADING);
				// fresh();
				// }
				// }
			}
		});
		selfaty_atta.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppConfig.INTENT_ACTION_SEND);
				intent.putExtra("mode", "new");
				intent.putExtra("id", id);
				intent.putExtra("at", userVo.getName());
				startActivity(intent);
				finish();
			}
		});

	}

	ArrayList<StatusVo> statusVos = null;
	StatusExpandableAdapter statusAdapter;
	private long since_id = 0l;
	private Context context;
	int count_progress = 0;
	private Handler hanlder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (msg.obj != null) {
					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						JSONArray jsonArray = jsonObject
								.getJSONArray("statuses");
						statusVos = (ArrayList<StatusVo>) JSONHelper
								.parseCollection(jsonArray, ArrayList.class,
										StatusVo.class);
						since_id = statusVos.get(0).getId();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				count_progress++;
				if (count_progress == 3) {
					progressBar.setVisibility(View.INVISIBLE);
				}
				statusAdapter = new StatusExpandableAdapter(statusVos, context);
				selfaty_listview.setAdapter(statusAdapter);
				// setListner();

				Log.d("RESULT", "onFooterRefresh");

			} else if (msg.what == 3) {

				if (msg.obj != null) {

					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						JSONArray jsonArray = jsonObject
								.getJSONArray("statuses");
						statusVos.addAll((ArrayList<StatusVo>) JSONHelper
								.parseCollection(jsonArray, ArrayList.class,
										StatusVo.class));
						Log.d("RESULT", statusVos.size() + "statusVos.size()");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				statusAdapter.notifyDataSetChanged();
				puRefreshView.onFooterRefreshComplete();
			} else if (msg.what == 2) {
				if (msg.obj != null) {

					try {
						JSONObject jsonObject = new JSONObject((String) msg.obj);
						JSONArray jsonArray = jsonObject
								.getJSONArray("statuses");
						ArrayList<StatusVo> cache = new ArrayList<StatusVo>();
						cache.addAll((ArrayList<StatusVo>) JSONHelper
								.parseCollection(jsonArray, ArrayList.class,
										StatusVo.class));
						cache.addAll(statusVos);
						statusVos = cache;
						Log.d("RESULT", statusVos.size()
								+ "------------statusVos.size()");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				statusAdapter.reSetData(statusVos);
				statusAdapter.notifyDataSetChanged();
				puRefreshView.onHeaderRefreshComplete();
			}

		}
	};

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		Log.d("RESULT", "onFooterRefresh");
		puRefreshView = view;
		statusesAPI.userTimeline(Long.parseLong(id), 0l, 0l, 20, count++,
				false, FEATURE.ALL, false, new RequestListener() {

					@Override
					public void onIOException(IOException arg0) {
						Log.d("RESULT", "arg0:    ||   " + arg0);

					}

					@Override
					public void onError(WeiboException arg0) {
						Log.d("RESULT", "arg0:    ||   " + arg0);

					}

					@Override
					public void onComplete(String arg0) {
						Message message = new Message();
						message.obj = arg0;
						message.what = 3;
						hanlder.sendMessage(message);

					}
				});

	}

	PullToRefreshView puRefreshView;

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		puRefreshView = view;
		statusesAPI.userTimeline(Long.parseLong(id), since_id, 0l, 20, 1,
				false, FEATURE.ALL, false, new RequestListener() {
					@Override
					public void onIOException(IOException arg0) {
						Log.d("RESULT", "arg0:    ||   " + arg0);
					}

					@Override
					public void onError(WeiboException arg0) {
						Log.d("RESULT", "arg0:    ||   " + arg0);
					}

					@Override
					public void onComplete(String arg0) {
						Message message = new Message();
						message.obj = arg0;
						message.what = 2;
						hanlder.sendMessage(message);
					}
				});
	}

	@Override
	public void onClick(View v) {
		Log.d("RESULT", "onclick");
		int idx = v.getId();
		switch (idx) {
		case R.id.selfaty_fans_layout:
			Intent intent = new Intent(AppConfig.INTENT_ACTION_FRIENDSLIST);
			intent.putExtra("mode", "fans");
			intent.putExtra("id", id);
			startActivity(intent);
			break;
		case R.id.selfaty_focus_layout:
			Intent intent2 = new Intent(AppConfig.INTENT_ACTION_FRIENDSLIST);
			intent2.putExtra("mode", "focus");
			intent2.putExtra("id", id);
			startActivity(intent2);
			break;
		default:
			break;
		}

	}

	private void focus() {

		progressBar.setVisibility(View.VISIBLE);
		friendshipsAPI.create(Long.parseLong(id), "", new RequestListener() {

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
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						selfaty_relationship.setText("取消关注");
						selfaty_relationship.setBackgroundColor(getResources()
								.getColor(R.color.button_green));
						selfaty_atta.setText("@TA");
						selfaty_relationship
								.setOnClickListener(new TextView.OnClickListener() {

									@Override
									public void onClick(View v) {
										release();
									}
								});
						progressBar.setVisibility(View.GONE);

					}
				});

			}
		});
	}

	private void release() {
		progressBar.setVisibility(View.VISIBLE);

		friendshipsAPI.destroy(Long.parseLong(id), "", new RequestListener() {

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
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						selfaty_relationship.setText("关注TA");
						selfaty_relationship.setBackgroundColor(getResources()
								.getColor(R.color.button_red));
						selfaty_atta.setText("@TA");
						selfaty_relationship
								.setOnClickListener(new TextView.OnClickListener() {

									@Override
									public void onClick(View v) {
										focus();
									}
								});
						progressBar.setVisibility(View.GONE);
					};
				});

			}
		});
	}

}
