/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.loftcat.ui.utils.slidingmenu.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.app.AppContext;
import com.loftcat.ui.Homepage;
import com.loftcat.ui.adapter.ExpandableAdapter;
import com.loftcat.ui.adapter.GroupsGalleryAdapter;
import com.loftcat.ui.utils.PullToRefreshView;
import com.loftcat.ui.utils.PullToRefreshView.OnFooterRefreshListener;
import com.loftcat.ui.utils.PullToRefreshView.OnHeaderRefreshListener;
import com.loftcat.utils.JSONHelper;
import com.loftcat.utils.LogCenter;
import com.loftcat.utils.SoundCenter;
import com.loftcat.utils.Utility;
import com.loftcat.weibo.sdk.FriendshipsAPI;
import com.loftcat.weibo.sdk.StatusesAPI;
import com.loftcat.weibo.sdk.WeiboAPI.FEATURE;
import com.loftcat.weibo.vo.AccountVo;
import com.loftcat.weibo.vo.GroupsVo;
import com.loftcat.weibo.vo.StatusVo;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.LinearGradient;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenuItem;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class MiddleFragment extends Fragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private ExpandableListView listView;
	private AccountVo account;
	private Utility utility;
	private ExpandableAdapter statusAdapter;
	PullToRefreshView pullToRefreshView;
	private Spinner title_bar;
	private RelativeLayout title_layout;
	private ProgressBar title_progressbar;

	// private LinearLayout edit_layout;
	// private TextView edit_at;
	// private TextView edit_new;
	// private TextView edit_exit;
	TranslateAnimation left, right;
	private SoundCenter soundCenter;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public Utility getUtility() {
		return utility;
	}

	public void setUtility(Utility utility) {
		this.utility = utility;
	}

	public AccountVo getAccount() {
		return account;
	}

	StatusesAPI statusesAPI;
	FriendshipsAPI friendshipsAPI;
	int x = 0;

	public void setAccount(AccountVo account) {
		this.account = account;
		Log.d("RESULT", account.getId() + "------------------ id");
		if (x != 0) {
			title_bar.setVisibility(View.INVISIBLE);
			title_progressbar.setVisibility(View.VISIBLE);

		}
		Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken(
				account.getToken(), account.getExpires_in());
		oauth2AccessToken.setExpiresIn(account.getExpires_in());
		utility.setAccessToken(oauth2AccessToken);
		Log.e("error", "getExpiresTime:"
				+ utility.getAccessToken().getExpiresTime());
		statusesAPI = new StatusesAPI(utility.getAccessToken());
		//
		//
		// statusesAPI.friendsTimeline(since_id, 0l, 20, count++, false,
		// FEATURE.ALL, false, new RequestListener() {
		//
		// @Override
		// public void onIOException(IOException arg0) {
		// Log.d("RESULT", "arg0:    ||   " + arg0);
		//
		// }
		//
		// @Override
		// public void onError(WeiboException arg0) {
		// Log.d("RESULT", "arg0:    ||   " + arg0);
		//
		// }
		//
		// @Override
		// public void onComplete(String arg0) {
		// Message message = new Message();
		// message.obj = arg0;
		// message.what = 1;
		// hanlder.sendMessage(message);
		// }
		// });

		friendshipsAPI = new FriendshipsAPI(utility.getAccessToken());
		Log.d("RESULT", "friendshipsAPI.groups");
		friendshipsAPI.groups(new RequestListener() {

			@Override
			public void onIOException(IOException arg0) {
				Log.d("RESULT", arg0.toString());
			}

			@Override
			public void onError(WeiboException arg0) {
				Log.d("RESULT", arg0.toString());
			}

			@Override
			public void onComplete(String arg0) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(arg0);
					JSONArray jsonArray = jsonObject.getJSONArray("lists");
					groups = new ArrayList<GroupsVo>();
					GroupsVo groupsVo = new GroupsVo();
					groupsVo.setName("全部");
					groupsVo.setId(0);
					groups.add(groupsVo);
					groups.addAll((ArrayList<GroupsVo>) JSONHelper
							.parseCollection(jsonArray, ArrayList.class,
									GroupsVo.class));
					Log.d("RESULT", groups.size() + "size");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 10;
				msg.arg1 = x;
				hanlder.sendMessage(msg);
			}
		});

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		int selectedId = Utility.getBackgroundId(context);
		background.setImageResource(AppConfig.backgrounds[selectedId]
				.getLargeImage());

	}

	View view;
	RelativeLayout relativeLayout;
	private static final int EXIT = 5;
	private static final int DRAW = 4;
	private static final int AT = 3;
	private static final int NEW = 2;
	private static final int SETTING = 1;
	ImageView background;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.middle, null);
		background = (ImageView) view
				.findViewById(R.id.middle_background_imageview);
		listView = (ExpandableListView) view
				.findViewById(R.id.status_expandablelistview);
		pullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.homepage_pulltorefreshview);
		relativeLayout = (RelativeLayout) view
				.findViewById(R.id.middle_background);
		Satellite();
		title_bar = (Spinner) view.findViewById(R.id.title_bar);

		title_bar.setVisibility(View.INVISIBLE);
		title_layout = (RelativeLayout) view.findViewById(R.id.title_layout);
		title_progressbar = (ProgressBar) view
				.findViewById(R.id.title_progressbar);
		pullToRefreshView.setOnHeaderRefreshListener(this);
		pullToRefreshView.setOnFooterRefreshListener(this);
		Log.d("RESULT", "----" + (utility.getAccessToken() == null));
		GroupsVo groupsVo = new GroupsVo();
		groupsVo.setName("全部");
		groupsVo.setId(0);
		groups.add(groupsVo);
		soundCenter = new SoundCenter(getActivity(), true);
		return view;
	}

	private ArrayAdapter<String> adapter;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void passContext(Context context) {
		this.context = context;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		soundCenter.release();
	}

	private void Satellite() {
		SatelliteMenu menu = (SatelliteMenu) view.findViewById(R.id.menu);
		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();

		// Set from XML, possible to programmatically set
		float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				170, getResources().getDisplayMetrics());
		menu.setSatelliteDistance((int) distance);
		menu.setExpandDuration(500);
		menu.setCloseItemsOnClick(true);
		menu.setTotalSpacingDegree(90);

		items.add(new SatelliteMenuItem(1, R.drawable.setting));
		items.add(new SatelliteMenuItem(2, R.drawable.edit));
		items.add(new SatelliteMenuItem(3, R.drawable.at));
		items.add(new SatelliteMenuItem(4, R.drawable.paint));
		items.add(new SatelliteMenuItem(5, R.drawable.exit));

		// items.add(new SatelliteMenuItem(6, R.drawable.ic_2));
		// items.add(new SatelliteMenuItem(5, R.drawable.sat_item));
		menu.addItems(items);

		menu.setOnItemClickedListener(new SateliteClickedListener() {

			public void eventOccured(int id) {
				Log.i("sat", "Clicked on " + id);
				switch (id) {
				case EXIT:
					((Homepage) context).finish();
					break;
				case DRAW:
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							Intent intent2 = new Intent(
									AppConfig.INTENT_ACTION_SPEN_EDITOR);
							intent2.putExtra("mode", "new");
							intent2.putExtra("userID",
									Long.valueOf(account.getId()));
							startActivity(intent2);
						}
					}, 500);
					break;
				case AT:
					timer.schedule(new TimerTask() {

						@Override
						public void run() {

							Intent intent = new Intent(
									AppConfig.INTENT_ACTION_AT);
							intent.putExtra("userID",
									Long.valueOf(account.getId()));
							intent.putExtra("mode", "new");
							startActivity(intent);
						}
					}, 500);

					break;
				case NEW:
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							Intent intent2 = new Intent(
									AppConfig.INTENT_ACTION_SEND);
							intent2.putExtra("mode", "new");
							intent2.putExtra("userID",
									Long.valueOf(account.getId()));
							startActivity(intent2);
						}
					}, 500);

					break;
				case SETTING:
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							Intent intent2 = new Intent(
									AppConfig.INTENT_ACTION_SETTING);
							startActivity(intent2);
						}
					}, 500);
					break;
				default:
					break;
				}
			}
		});
	}

	Context context;
	ArrayList<StatusVo> statusVos = null;
	private long since_id = 0l;
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
						ArrayList<StatusVo> cache = (ArrayList<StatusVo>) JSONHelper
								.parseCollection(jsonArray, ArrayList.class,
										StatusVo.class);
						if (cache != null && cache.size() > 0) {
							statusVos = cache;
							since_id = statusVos.get(0).getId();
							group_index = msg.arg1;
							// title_bar
							// .setText(groups.get(group_index).getName());
							title_bar.setVisibility(View.VISIBLE);
							title_progressbar.setVisibility(View.INVISIBLE);
							TranslateAnimation translateAnimation = new TranslateAnimation(
									0, 0, -title_layout.getHeight(), 0);
							translateAnimation.setDuration(1000);
							translateAnimation
									.setInterpolator(new BounceInterpolator());
							title_layout.startAnimation(translateAnimation);

						} else {
							title_bar.setVisibility(View.VISIBLE);
							title_progressbar.setVisibility(View.INVISIBLE);
							Toast.makeText(context, "对不起~没有该分组下的微博~",
									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				// statusAdapter.reSetData(statusVos);
				statusAdapter = new ExpandableAdapter(statusVos, context);
				listView.setAdapter(statusAdapter);
				setListner();
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
						since_id = statusVos.get(0).getId();
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
			} else if (msg.what == 10) {
				Log.d("RESULT", "msg.what == 10");
				// groupsGalleryAdapter.reSetData(groups);
				// groupsGalleryAdapter.notifyDataSetChanged();
				String[] m = new String[groups.size()];
				for (int i = 0; i < groups.size(); i++) {
					m[i] = groups.get(i).getName();
				}
				if (m != null) {
					adapter = new ArrayAdapter<String>(getActivity(),
							android.R.layout.simple_spinner_item, m);

					// 设置下拉列表的风格
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// 将adapter 添加到spinner中
					title_bar.setAdapter(adapter);

					// 添加事件Spinner事件监听
					title_bar
							.setOnItemSelectedListener(new SpinnerSelectedListener());

					if (x == 0) {
						title_bar.setVisibility(View.VISIBLE);
						title_progressbar.setVisibility(View.INVISIBLE);
						TranslateAnimation translateAnimation = new TranslateAnimation(
								0, 0, -title_layout.getHeight(), 0);
						translateAnimation.setDuration(1000);
						translateAnimation
								.setInterpolator(new BounceInterpolator());
						title_layout.startAnimation(translateAnimation);
						x++;
					}

				}

			} else if (msg.what == -1) {
				// TranslateAnimation translateAnimation = new
				// TranslateAnimation(
				// 0, edit_layout.getWidth(), 0, 0);
				// translateAnimation.setDuration(1000);
				// translateAnimation.setInterpolator(new BounceInterpolator());
				// edit_layout.startAnimation(translateAnimation);
				// edit_layout.setVisibility(View.GONE);
				// isShowEdit = false;
			}
			soundCenter.show();
		}
	};

	public long getSince_id() {
		return since_id;
	}

	public void setSince_id(long since_id) {
		this.since_id = since_id;
	}

	private ArrayList<GroupsVo> groups = new ArrayList<GroupsVo>();

	private void setListner() {
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView parent,
					View clickedView, int groupPosition, long groupId) {
				return false;
			}
		});
		listView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView expandablelistview,
					View clickedView, int groupPosition, int childPosition,
					long childId) {
				Log.d("RESULT", "child_click");
				return false;// 返回true表示此事件在此被处理了
			}
		});
		listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			public void onGroupCollapse(int groupPosition) {
			}
		});
		listView.setOnGroupExpandListener(new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < statusVos.size(); i++) {
					if (groupPosition != i) {
						listView.collapseGroup(i);

					}
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});

		listView.setOnScrollListener(new OnScrollListener() {

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

	PullToRefreshView puRefreshView;
	private int count = 1;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		puRefreshView = view;
		if (list_id == 0) {
			statusesAPI.friendsTimeline(0l, 0l, 20, count++, false,
					FEATURE.ALL, false, new RequestListener() {

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
		} else {
			friendshipsAPI.groups_timeline(list_id, 0, 0, 20, count++, 0, 0,
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
							Message message = new Message();
							message.obj = arg0;
							message.what = 3;
							hanlder.sendMessage(message);
						}
					});
		}

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		puRefreshView = view;
		if (list_id == 0) {
			statusesAPI.friendsTimeline(since_id, 0l, 20, 1, false,
					FEATURE.ALL, false, new RequestListener() {

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

		} else {
			friendshipsAPI.groups_timeline(list_id, since_id, 0, 20, 1, 0, 0,
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
							Message message = new Message();
							message.obj = arg0;
							message.what = 2;
							hanlder.sendMessage(message);
						}
					});
		}

	}

	Gallery groups_gallery;
	GroupsGalleryAdapter groupsGalleryAdapter;
	private int group_index;

	private long list_id;

	private void resetData(long id, final int index) {
		since_id = 0;
		count = 1;
		list_id = id;
		if (list_id != 0) {
			friendshipsAPI.groups_timeline(list_id, since_id, 0, 20, count++,
					0, 0, new RequestListener() {

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
							message.arg1 = index;
							hanlder.sendMessage(message);
						}
					});

		} else {
			statusesAPI.friendsTimeline(since_id, 0l, 20, count++, false,
					FEATURE.ALL, false, new RequestListener() {

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
							message.what = 1;
							hanlder.sendMessage(message);
						}
					});
		}

	}

	// 使用数组形式操作
	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (x != 0) {
				title_progressbar.setVisibility(View.VISIBLE);
				resetData(groups.get(arg2).getId(), arg2);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	boolean isShowEdit = false;
	Timer timer = new Timer();
}
