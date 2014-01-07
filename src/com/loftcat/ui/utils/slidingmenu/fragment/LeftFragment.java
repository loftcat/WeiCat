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
import java.net.MalformedURLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.Homepage;
import com.loftcat.ui.Loading;
import com.loftcat.ui.adapter.AccountAdapter;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.DBManager;
import com.loftcat.utils.LogCenter;
import com.loftcat.utils.Utility;
import com.loftcat.weibo.vo.AccountVo;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LeftFragment extends Fragment {
	private ListView _account_listview;
	private TextView _account_add_button;
	private AccountAdapter accountAdapter;
	private DBManager _dBManager;
	TextView self;
	TextView atme;
	TextView comments;
	TextView weibo;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		_account_listview = (ListView) view
				.findViewById(R.id.left_account_list);
		_account_add_button = (TextView) view
				.findViewById(R.id.left_accout_add_button);
		accountAdapter = new AccountAdapter(context, accounts,
				utility.readIndex());
		_account_listview.setAdapter(accountAdapter);
		imagelists = accountAdapter.getImagelists();
		self = (TextView) view.findViewById(R.id.left_self);
		weibo = (TextView) view.findViewById(R.id.left_weibo);
		comments = (TextView) view.findViewById(R.id.left_comments);
		atme = (TextView) view.findViewById(R.id.left_atme);
		setListener();

		return view;
	}

	public void passContext(Context context) {
		this.context = context;
	}

	List<AccountVo> accounts;

	public List<AccountVo> getAccounts() {
		return accounts;
	}

	public void setAccounts(DBManager mDBManager, List<AccountVo> accounts) {
		this._dBManager = mDBManager;
		this.accounts = accounts;
	}

	private Context context;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	Weibo mWeibo;

	public Weibo getmWeibo() {
		return mWeibo;
	}

	public void setmWeibo(Weibo mWeibo) {
		this.mWeibo = mWeibo;
	}

	private String delete_id;

	private void setListener() {
		_account_add_button.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				isAdd = true;
				mWeibo = Weibo
						.getInstance(BaseActivity.CONSUMER_KEY,
								BaseActivity.CONSUMER_SECRET,
								BaseActivity.REDIRECT_URL);
				mWeibo.authorize(context, new AuthDialogListener());
			}
		});
		_account_listview
				.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {

						if (accounts.size() > 1) {
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(
									context)
									.setTitle("提示")
									.setMessage("是否删除该账户？")
									.setPositiveButton("否",
											new AlertDialog.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// TODO Auto-generated
													// method stub

												}
											})
									.setNegativeButton("确定",
											new AlertDialog.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													isDelete = true;
													delete_id = accounts.get(
															arg2).getUid();
													_dBManager
															.deleteAccount(accounts
																	.get(arg2));
													Timer timer = new Timer();
													timer.schedule(
															new TimerTask() {

																@Override
																public void run() {
																	_handler.sendEmptyMessage(0);
																}
															}, 150);
													Toast.makeText(context,
															"已删除该账户~",
															Toast.LENGTH_LONG)
															.show();
												}
											});
							alertDialog.show();
						} else {

						}

						return false;
					}

				});
		_account_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				for (int i = 0; i < imagelists.size(); i++) {
					if (i == arg2) {
						imagelists.get(arg2).setVisibility(View.VISIBLE);
						utility.keepIndex(
								Long.valueOf(accounts.get(i).getId()), _handler);
					} else {
						imagelists.get(i).setVisibility(View.GONE);
					}
				}
				((Homepage) getActivity()).setSince_id(0l);
				((Homepage) getActivity()).setPage(1);
				((Homepage) getActivity()).setAccount(accounts.get(arg2));
				((Homepage) getActivity()).showRight();
			}

		});
		self.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("userID", ((Homepage) getActivity())
						.get_account().getId());
				intent.putExtra("self", true);
				intent.setAction(AppConfig.INTENT_ACTION_SELFPAGE);
				startActivity(intent);
			}
		});
		weibo.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((Homepage) getActivity()).showLeft();
			}
		});
		comments.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("com.loftcat.ui.CommentsListAty");
				intent.putExtra("mode", "all");
				startActivity(intent);
			}
		});
		atme.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("com.loftcat.ui.CommentsListAty");
				intent.putExtra("mode", "at");
				startActivity(intent);
			}
		});
	}

	boolean isAdd = false;
	boolean isDelete = false;
	private Oauth2AccessToken accessToken;
	private Utility utility;

	public Utility getUtility() {
		return utility;
	}

	public void setUtility(Utility utility) {
		this.utility = utility;
	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			Log.d("RESULT", "-------------------token:" + token
					+ "  expires_in:" + expires_in);
			accessToken = new Oauth2AccessToken(token, expires_in);
			utility.setAccessToken(accessToken);
			utility.loadUserData(accessToken, _handler, false);
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(context, "Auth error : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(context, "Auth cancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(context, "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}

	private List<ImageView> imagelists;

	Handler _handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what != -1) {
				if (isAdd) {
					accounts = _dBManager.getAccounts();
					accountAdapter = new AccountAdapter(context, accounts,
							utility.readIndex());
					_account_listview.setAdapter(accountAdapter);
					imagelists = accountAdapter.getImagelists();

					int size = accounts.size();
					long _index = utility.readIndex();
					AccountVo _account = null;
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							if (Long.valueOf(accounts.get(i).getUid()) == _index) {
								_account = accounts.get(i);
							}
						}
					}
					((Homepage) getActivity()).setSince_id(0l);
					((Homepage) getActivity()).setPage(1);
					((Homepage) getActivity()).setAccount(_account);
					((Homepage) getActivity()).showRight();
					isAdd = false;
					LogCenter.getInstance().debug("XXX", "add");
				}
				if (isDelete) {
					accounts = _dBManager.getAccounts();
					int size = accounts.size();
					long _index = utility.readIndex();
					AccountVo _account = null;
					if (!((Homepage) getActivity()).get_account().getId()
							.equals(delete_id)) {
						_account = ((Homepage) getActivity()).get_account();
					} else {
						_account = accounts.get(size - 1);
						utility.keepIndex(Long.parseLong(accounts.get(size - 1)
								.getUid()));
						Log.d("RESULT", "delete");
					}
					accountAdapter = new AccountAdapter(context, accounts,
							utility.readIndex());
					_account_listview.setAdapter(accountAdapter);
					imagelists = accountAdapter.getImagelists();
					((Homepage) getActivity()).setSince_id(0l);
					((Homepage) getActivity()).setPage(1);
					((Homepage) getActivity()).setAccount(_account);
					((Homepage) getActivity()).showRight();
					isDelete = false;
				}
			}
			super.handleMessage(msg);
		}
	};
}
