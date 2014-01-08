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
import android.os.Handler;
import android.os.Message;
import com.google.gson.reflect.TypeToken;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.google.gson.Gson;
import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.adapter.CommentsExpandableAdapter;
import com.loftcat.ui.adapter.FriendslistAdapter;
import com.loftcat.ui.adapter.ReportsExpandableAdapter;
import com.loftcat.ui.adapter.StatusExpandableAdapter;
import com.loftcat.ui.utils.PullToRefreshView;
import com.loftcat.ui.utils.PullToRefreshView.OnFooterRefreshListener;
import com.loftcat.ui.utils.PullToRefreshView.OnHeaderRefreshListener;
import com.loftcat.utils.cache.CacheManager;
import com.loftcat.utils.log.LogCenter;
import com.loftcat.utils.weibo.AccountManager;
import com.loftcat.weibo.sdk.CommentsAPI;
import com.loftcat.weibo.sdk.FriendshipsAPI;
import com.loftcat.weibo.sdk.StatusesAPI;
import com.loftcat.weibo.sdk.WeiboAPI.AUTHOR_FILTER;
import com.loftcat.weibo.sdk.WeiboAPI.FEATURE;
import com.loftcat.weibo.sdk.WeiboAPI.SRC_FILTER;
import com.loftcat.weibo.sdk.WeiboAPI.TYPE_FILTER;
import com.loftcat.weibo.vo.CommentVo;
import com.loftcat.weibo.vo.ReportsVo;
import com.loftcat.weibo.vo.StatusVo;
import com.loftcat.weibo.vo.UserVO;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class CommentsListAty extends BaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	PullToRefreshView pullToRefreshView;
	private Gson gson;
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

	ImageView commentlist_background;

	@Override
	public void initView() {
		setContentView(R.layout.expanlist);
		commentlist_background = (ImageView) findViewById(R.id.commentlist_background);

		friendsListView = (ExpandableListView) findViewById(R.id.friendslist_listview);
		progressBar = (ProgressBar) findViewById(R.id.comment_progress);
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.friends_pulltorefreshview);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterRefreshListener(this);
	}

	ProgressBar progressBar;
	CommentsAPI commentsAPI;
	StatusesAPI statusesAPI;
	CommentsExpandableAdapter commentsAdapter;
	StatusExpandableAdapter statusAdapter;
	ReportsExpandableAdapter reportsAdapter;

	ExpandableListView friendsListView;
	int count = 1;
	private int next_cursor = 0;
	private int previous_cursor = 0;
	ArrayList<CommentVo> commentVo = new ArrayList<CommentVo>();
	ArrayList<StatusVo> statusVos = new ArrayList<StatusVo>();
	ArrayList<ReportsVo> reportsVos = new ArrayList<ReportsVo>();

	private long minId = 0;
	String mode;
	private long status_id;

	@Override
	public void initLogic() {
		gson = new Gson();
		mode = getIntent().getStringExtra("mode");

		if (mode.equals("all")) {

			commentsAPI = new CommentsAPI(utility.getAccessToken());
			commentsAPI.timeline(0, 0, 20, count++, false,
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
										.getJSONArray("comments");
								commentVo.addAll((ArrayList<CommentVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<CommentVo>>(){}.getType()));
								
//								(ArrayList<UserVO>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<UserVO>>(){}.getType())
								minId = commentVo.get(0).getStatus().getId();
								Log.d("RESULT", "minId:" + minId);
								Message msg = new Message();

								msg.what = 1;
								msg.obj = commentVo;
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
		} else if (mode.equals("at")) {
			Log.d("RESULT", "at");
			statusesAPI = new StatusesAPI(utility.getAccessToken());
			statusesAPI.mentions(0l, 0l, 20, count++, AUTHOR_FILTER.ALL,
					SRC_FILTER.ALL, TYPE_FILTER.ALL, false,
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
										.getJSONArray("statuses");
								statusVos.addAll((ArrayList<StatusVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<StatusVo>>(){}.getType()));
//								commentVo.addAll((ArrayList<CommentVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<CommentVo>>(){}.getType()));

								if (statusVos.size() > 0) {
									minId = statusVos.get(0).getId();
									Message msg = new Message();
									msg.what = 1;
									msg.obj = statusVos;
									handler.sendMessage(msg);
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

		} else if (mode.equals("the_comments")) {
			commentsAPI = new CommentsAPI(utility.getAccessToken());
			status_id = getIntent().getLongExtra("status_id", 0);
			commentsAPI.show(status_id, 0, 0, 20, count++, AUTHOR_FILTER.ALL,
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
										.getJSONArray("comments");
								commentVo.addAll((ArrayList<CommentVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<CommentVo>>(){}.getType()));
								minId = commentVo.get(0).getStatus().getId();
								Message msg = new Message();
								msg.what = 1;
								msg.obj = commentVo;
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

		} else if (mode.equals("reports")) {
			statusesAPI = new StatusesAPI(utility.getAccessToken());
			status_id = getIntent().getLongExtra("status_id", 0);
			statusesAPI.repostTimeline(status_id, 0, 0, 20, count++,
					AUTHOR_FILTER.ALL, new RequestListener() {

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
										.getJSONArray("reposts");
//								reportsVos.addAll(JSONHelper.parseCollection(
//										jsonarray, ArrayList.class,
//										ReportsVo.class));
								reportsVos.addAll((ArrayList<ReportsVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<ReportsVo>>(){}.getType()));

								if (reportsVos.size() > 0) {
									minId = reportsVos.get(0).getId();
									Message msg = new Message();
									msg.what = 1;
									msg.obj = reportsVos;
									handler.sendMessage(msg);
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		commentlist_background.setImageResource(CacheManager.backgrounds[CacheManager.getBackGroundCache()
				.getBackgroundId(this)].getLargeImage());
	}

	@Override
	public void initListener() {
		friendsListView.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView parent,
					View clickedView, int groupPosition, long groupId) {
				return false;
			}
		});
		friendsListView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView expandablelistview,
					View clickedView, int groupPosition, int childPosition,
					long childId) {
				Log.d("RESULT", "child_click");
				return false;// 返回true表示此事件在此被处理了
			}
		});
		friendsListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					public void onGroupCollapse(int groupPosition) {
					}
				});
		friendsListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {

				if (mode.equals("at")) {
					for (int i = 0; i < statusVos.size(); i++) {
						if (groupPosition != i) {
							friendsListView.collapseGroup(i);

						}
					}
				} else if (mode.equals("all")) {
					for (int i = 0; i < commentVo.size(); i++) {
						if (groupPosition != i) {
							friendsListView.collapseGroup(i);
						}
					}
				} else if (mode.equals("the_comments")) {
					for (int i = 0; i < commentVo.size(); i++) {
						if (groupPosition != i) {
							friendsListView.collapseGroup(i);
						}
					}
				} else if (mode.equals("reports")) {
					for (int i = 0; i < reportsVos.size(); i++) {
						if (groupPosition != i) {
							friendsListView.collapseGroup(i);
						}
					}
				}

			}
		});
		friendsListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		friendsListView.setOnScrollListener(new OnScrollListener() {

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
	}

	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.obj != null) {
				if (mode.equals("all")) {
					if (msg.what == 1) {
						commentsAdapter = new CommentsExpandableAdapter(
								(ArrayList<CommentVo>) msg.obj,
								CommentsListAty.this);
						friendsListView.setAdapter(commentsAdapter);
					} else if (msg.what == 3) {
						commentVo.addAll((ArrayList<CommentVo>) msg.obj);
						commentsAdapter.notifyDataSetChanged();
						puRefreshView.onFooterRefreshComplete();
					} else if (msg.what == 2) {
						ArrayList<CommentVo> cache = new ArrayList<CommentVo>();
						cache.addAll((Collection<? extends CommentVo>) msg.obj);
						cache.addAll(commentVo);
						commentVo = cache;
						commentsAdapter.notifyDataSetChanged();
						puRefreshView.onHeaderRefreshComplete();
						commentsAdapter.reSetData(commentVo);
						friendsListView.invalidate();
					}

				} else if (mode.equals("at")) {
					if (msg.what == 1) {
						statusAdapter = new StatusExpandableAdapter(
								(ArrayList<StatusVo>) msg.obj,
								CommentsListAty.this);
						friendsListView.setAdapter(statusAdapter);
					} else if (msg.what == 3) {
						statusVos.addAll((ArrayList<StatusVo>) msg.obj);
						statusAdapter.notifyDataSetChanged();
						puRefreshView.onFooterRefreshComplete();
					} else if (msg.what == 2) {
						ArrayList<StatusVo> cache = new ArrayList<StatusVo>();
						cache.addAll((ArrayList	< StatusVo>) msg.obj);
						cache.addAll(statusVos);
						statusVos = cache;
						statusAdapter.notifyDataSetChanged();
						puRefreshView.onHeaderRefreshComplete();
						statusAdapter.reSetData(statusVos);
						friendsListView.invalidate();
					}
				} else if (mode.equals("the_comments")) {
					if (msg.what == 1) {
						commentsAdapter = new CommentsExpandableAdapter(
								(ArrayList<CommentVo>) msg.obj,
								CommentsListAty.this);
						friendsListView.setAdapter(commentsAdapter);
					} else if (msg.what == 3) {
						commentVo.addAll((ArrayList<CommentVo>) msg.obj);
						commentsAdapter.notifyDataSetChanged();
						puRefreshView.onFooterRefreshComplete();
					} else if (msg.what == 2) {
						ArrayList<CommentVo> cache = new ArrayList<CommentVo>();
						cache.addAll((ArrayList< CommentVo>) msg.obj);
						cache.addAll(commentVo);
						commentVo = cache;
						commentsAdapter.notifyDataSetChanged();
						puRefreshView.onHeaderRefreshComplete();
						commentsAdapter.reSetData(commentVo);
						friendsListView.invalidate();
					}
				} else if (mode.equals("reports")) {
					if (msg.what == 1) {
						reportsAdapter = new ReportsExpandableAdapter(
								(ArrayList<ReportsVo>) msg.obj,
								CommentsListAty.this);
						friendsListView.setAdapter(reportsAdapter);
					} else if (msg.what == 3) {
						reportsVos.addAll((ArrayList<ReportsVo>) msg.obj);
						reportsAdapter.notifyDataSetChanged();
						puRefreshView.onFooterRefreshComplete();
					} else if (msg.what == 2) {
						ArrayList<ReportsVo> cache = new ArrayList<ReportsVo>();
						cache.addAll((ArrayList< ReportsVo>) msg.obj);
						cache.addAll(reportsVos);
						reportsVos = cache;
						reportsAdapter.notifyDataSetChanged();
						puRefreshView.onHeaderRefreshComplete();
						reportsAdapter.reSetData(reportsVos);
						friendsListView.invalidate();
					}
				}
				progressBar.setVisibility(View.GONE);
			}
		}

	};
	PullToRefreshView puRefreshView;

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		Log.d("RESULT", "onFooterRefresh");
		puRefreshView = view;
		if (mode.equals("all")) {
			commentsAPI.timeline(0, 0, 20, count++, false,
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
										.getJSONArray("comments");
								ArrayList<CommentVo> cache =(ArrayList<CommentVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<CommentVo>>(){}.getType());
								Message msg = new Message();
								msg.what = 3;
								msg.obj = cache;
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
		} else if (mode.equals("at")) {
			statusesAPI.mentions(0l, 0l, 20, count++, AUTHOR_FILTER.ALL,
					SRC_FILTER.ALL, TYPE_FILTER.ALL, false,
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
										.getJSONArray("statuses");
								ArrayList<StatusVo> cache = (ArrayList<StatusVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<StatusVo>>(){}.getType());
								Message msg = new Message();
								msg.what = 3;
								msg.obj = cache;
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
		} else if (mode.equals("the_comments")) {
			commentsAPI.show(status_id, 0, 0, 20, count++, AUTHOR_FILTER.ALL,
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
										.getJSONArray("comments");
								ArrayList<CommentVo> cache =(ArrayList<CommentVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<CommentVo>>(){}.getType());
								Message msg = new Message();
								msg.what = 3;
								msg.obj = cache;
								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
		} else if (mode.equals("reports")) {
			statusesAPI.repostTimeline(status_id, 0l, 0l, 20, count++,
					AUTHOR_FILTER.ALL, new RequestListener() {

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
										.getJSONArray("reposts");
//								ArrayList<ReportsVo> cache = (ArrayList<ReportsVo>) JSONHelper
//										.parseCollection(jsonarray,
//												ArrayList.class,
//												ReportsVo.class);
								ArrayList<ReportsVo> cache =(ArrayList<ReportsVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<ReportsVo>>(){}.getType());

								Message msg = new Message();
								msg.what = 3;
								msg.obj = cache;
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
		if (mode.equals("all")) {
			commentsAPI.timeline(minId, 0, 20, count++, false,
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
										.getJSONArray("comments");
//								ArrayList<CommentVo> cache = (ArrayList<CommentVo>) JSONHelper
//										.parseCollection(jsonarray,
//												ArrayList.class,
//												CommentVo.class);
								ArrayList<CommentVo> cache =(ArrayList<CommentVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<CommentVo>>(){}.getType());

								if (cache.size() > 0) {
									minId = cache.get(0).getStatus().getId();
									Message msg = new Message();
									msg.what = 2;
									msg.obj = cache;
									handler.sendMessage(msg);
								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											puRefreshView
													.onHeaderRefreshComplete();
										}
									});
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
		} else if (mode.equals("at")) {
			statusesAPI.mentions(minId, 0l, 20, count++, AUTHOR_FILTER.ALL,
					SRC_FILTER.ALL, TYPE_FILTER.ALL, false,
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
										.getJSONArray("statuses");
//								ArrayList<StatusVo> cache = (ArrayList<StatusVo>) JSONHelper
//										.parseCollection(jsonarray,
//												ArrayList.class, StatusVo.class);
								ArrayList<StatusVo> cache =(ArrayList<StatusVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<StatusVo>>(){}.getType());
								if (cache.size() > 0) {
									minId = cache.get(0).getId();
									Message msg = new Message();
									msg.what = 2;
									msg.obj = cache;
									handler.sendMessage(msg);
								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											puRefreshView
													.onHeaderRefreshComplete();
										}
									});
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
		} else if (mode.equals("the_comments")) {
			commentsAPI.show(status_id, minId, 0, 20, count++,
					AUTHOR_FILTER.ALL, new RequestListener() {

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
										.getJSONArray("comments");
								
//								ArrayList<CommentVo> cache = (ArrayList<CommentVo>) JSONHelper
//										.parseCollection(jsonarray,
//												ArrayList.class,
//												CommentVo.class);
								ArrayList<CommentVo> cache =(ArrayList<CommentVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<CommentVo>>(){}.getType());

								if (cache.size() > 0) {
									minId = cache.get(0).getStatus().getId();
									Message msg = new Message();
									msg.what = 2;
									msg.obj = cache;
									handler.sendMessage(msg);
								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											puRefreshView
													.onHeaderRefreshComplete();
										}
									});
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
		} else if (mode.equals("reports")) {
			statusesAPI.repostTimeline(status_id, minId, 0l, 20, count++,
					AUTHOR_FILTER.ALL, new RequestListener() {

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
										.getJSONArray("reposts");
//								ArrayList<ReportsVo> cache = (ArrayList<ReportsVo>) JSONHelper
//										.parseCollection(jsonarray,
//												ArrayList.class,
//												ReportsVo.class);
								ArrayList<ReportsVo> cache =(ArrayList<ReportsVo>)gson.fromJson( jsonarray.toString(), new TypeToken<ArrayList<ReportsVo>>(){}.getType());								
								
								if (cache.size() > 0) {
									minId = cache.get(0).getId();
									Message msg = new Message();
									msg.what = 2;
									msg.obj = cache;
									handler.sendMessage(msg);
								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											puRefreshView
													.onHeaderRefreshComplete();
										}
									});
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
		}

	}

}
