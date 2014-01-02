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
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.utils.MyListView;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.JSONHelper;
import com.loftcat.utils.LogCenter;
import com.loftcat.utils.TimeUtil;
import com.loftcat.weibo.bean.StatusVo;
import com.loftcat.weibo.bean.UserVO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

public class TheStatus extends BaseActivity {
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	// long id;
	Bundle bundle;
	String from;

	StatusVo statusVo;
	UserVO userVO;
	private ImageView head;
	private TextView name;
	TextView msg;
	ImageView pic;
	TextView retweeted_msg;
	TextView retweeted_replay;
	TextView retweeted_forward;
	RelativeLayout retweeted_layout;
	ImageView retweeted_pic;
	TextView time;
	TextView source;
	TextView replay;
	TextView forward;

	TextView do_forward;
	TextView do_comment;

	private RelativeLayout thestatus_user_layout;

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		setContentView(R.layout.thestatus);
		head = (ImageView) findViewById(R.id.thestatus_user_head);
		name = (TextView) findViewById(R.id.thestatus_user_name);
		thestatus_user_layout = (RelativeLayout) findViewById(R.id.thestatus_user_layout);
		msg = (TextView) findViewById(R.id.status_item_parent_msg);
		pic = (ImageView) findViewById(R.id.status_item_parent_pic);
		retweeted_msg = (TextView) findViewById(R.id.status_item_parent_retweeted_status_text);
		retweeted_replay = (TextView) findViewById(R.id.status_item_parent_retweeted_status_reply);
		retweeted_forward = (TextView) findViewById(R.id.status_item_parent_retweeted_status_forward);
		retweeted_layout = (RelativeLayout) findViewById(R.id.status_item_parent_retweeted_layout);
		retweeted_pic = (ImageView) findViewById(R.id.status_item_parent_retweeted_status_pic);
		do_forward = (TextView) findViewById(R.id.forward);
		do_comment = (TextView) findViewById(R.id.comment);
		time = (TextView) findViewById(R.id.status_item_parent_time);
		source = (TextView) findViewById(R.id.status_item_parent_source);
		replay = (TextView) findViewById(R.id.status_item_parent_reply);
		forward = (TextView) findViewById(R.id.status_item_parent_forward);
		bundle = getIntent().getBundleExtra("status");
		from = bundle.getString("from");
		statusVo = (StatusVo) bundle.getSerializable("statusVo");
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.head_default).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(0)).build();
	}

	@Override
	public void initLogic() {
		if (from == null) {
			userVO = statusVo.getUser();

			name.setText(userVO.getName());
			imageLoader.displayImage(userVO.getProfile_image_url(), head,
					options);
			msg.setText(statusVo.getText());
			if (statusVo.getOriginal_pic() != null) {
				pic.setVisibility(View.VISIBLE);

				imageLoader.displayImage(statusVo.getOriginal_pic(), pic,
						options);
				pic.setOnClickListener(new ImageView.OnClickListener() {

					@Override
					public void onClick(View v) {
						String uri = imageLoader.getLoadingUriForView(pic);
						Intent intent = new Intent(
								AppConfig.INTENT_ACTION_GESTUREIMAGEVIEW);
						intent.putExtra("path", statusVo.getOriginal_pic());
						intent.putExtra("userID", statusVo.getId());

						// Log.d("RESULT", "uri:   " + uri);
						startActivity(intent);
					}
				});
			} else if (statusVo.getBmiddle_pic() != null) {
				pic.setVisibility(View.VISIBLE);

				imageLoader.displayImage(statusVo.getBmiddle_pic(), pic,
						options);
			} else if (statusVo.getThumbnail_pic() != null) {
				pic.setVisibility(View.VISIBLE);

				imageLoader.displayImage(statusVo.getThumbnail_pic(), pic,
						options);
			} else {
				pic.setVisibility(View.GONE);

			}
			time.setText(TimeUtil.converTime(new Date(statusVo.getCreated_at())
					.getTime() / 1000));
			replay.setText("回复" + "("
					+ String.valueOf(statusVo.getComments_count()) + ")");
			forward.setText("转发" + "("
					+ String.valueOf(statusVo.getReposts_count()) + ")");
			source.setText("来自：" + Html.fromHtml(statusVo.getSource()));

			if (statusVo.getRetweeted_status() != null) {
				retweeted_layout.setVisibility(View.VISIBLE);
				Log.d("RESULT", (retweeted_msg == null) + "msg");
				retweeted_msg.setText(((statusVo.getRetweeted_status()
						.getUser() == null) ? "" : (statusVo
						.getRetweeted_status().getUser().getName() + ":  "))
						+ (statusVo.getRetweeted_status().getText()));

				retweeted_replay.setText("回复"
						+ "("
						+ String.valueOf(statusVo.getRetweeted_status()
								.getComments_count()) + ")");
				retweeted_forward.setText("转发"
						+ "("
						+ String.valueOf(statusVo.getRetweeted_status()
								.getReposts_count()) + ")");

				if (statusVo.getRetweeted_status().getOriginal_pic() != null) {
					retweeted_pic.setVisibility(View.VISIBLE);
					imageLoader.displayImage(statusVo.getRetweeted_status()
							.getOriginal_pic(), retweeted_pic, options);
				} else if (statusVo.getRetweeted_status().getBmiddle_pic() != null) {
					retweeted_pic.setVisibility(View.VISIBLE);

					imageLoader.displayImage(statusVo.getRetweeted_status()
							.getBmiddle_pic(), retweeted_pic, options);
				} else if (statusVo.getRetweeted_status().getThumbnail_pic() != null) {
					retweeted_pic.setVisibility(View.VISIBLE);

					imageLoader.displayImage(statusVo.getRetweeted_status()
							.getThumbnail_pic(), retweeted_pic, options);
				} else {
					retweeted_pic.setVisibility(View.GONE);
				}
				retweeted_layout
						.setOnClickListener(new RelativeLayout.OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(
										"com.loftcat.ui.TheStatus");
								Bundle budBundle = new Bundle();
								budBundle.putSerializable("statusVo",
										statusVo.getRetweeted_status());
								intent.putExtra("status", budBundle);
								startActivity(intent);
							}
						});
			} else {
				retweeted_layout.setVisibility(View.GONE);
			}
		} else {
			statusesAPI = new StatusesAPI(utility.getAccessToken());

			statusesAPI.show(statusVo.getId(), new RequestListener() {

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
						statusVo = JSONHelper.parseObject(arg0, StatusVo.class);
						userVO = statusVo.getUser();
						handler.sendEmptyMessage(0);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		}
	}

	StatusesAPI statusesAPI;

	@Override
	public void initListener() {
		replay.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (statusVo.getComments_count() == 0) {
					Toast.makeText(TheStatus.this, "该微博还没有回复~",
							Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(AppConfig.INTENT_ACTION_COMMENTS);
					intent.putExtra("mode", "the_comments");
					intent.putExtra("status_id", statusVo.getId());
					intent.putExtra("userID", statusVo.getUser().getId());
					startActivity(intent);
				}
			}

		});
		forward.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (statusVo.getComments_count() == 0) {
					Toast.makeText(TheStatus.this, "该微博还没有转发~",
							Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(AppConfig.INTENT_ACTION_COMMENTS);
					intent.putExtra("mode", "reports");
					intent.putExtra("status_id", statusVo.getId());
					intent.putExtra("userID", statusVo.getUser().getId());
					startActivity(intent);
				}
			}
		});
		do_forward.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppConfig.INTENT_ACTION_SEND);
				intent.putExtra("status_id", statusVo.getId());
				intent.putExtra("userID", statusVo.getUser().getId());
				intent.putExtra("mode", "forward");
				TheStatus.this.startActivity(intent);
			}
		});
		do_comment.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppConfig.INTENT_ACTION_SEND);
				intent.putExtra("status_id", statusVo.getId());
				intent.putExtra("userID", statusVo.getUser().getId());
				intent.putExtra("mode", "comment");
				TheStatus.this.startActivity(intent);
			}
		});
		thestatus_user_layout
				.setOnClickListener(new RelativeLayout.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.putExtra("userID",
								String.valueOf(statusVo.getUser().getId()));
						intent.setAction(AppConfig.INTENT_ACTION_SELFPAGE);
						TheStatus.this.startActivity(intent);
					}
				});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msgx) {
			super.handleMessage(msgx);
			name.setText(userVO.getName());
			imageLoader.displayImage(userVO.getProfile_image_url(), head,
					options);
			msg.setText(statusVo.getText());
			if (statusVo.getOriginal_pic() != null) {
				pic.setVisibility(View.VISIBLE);

				imageLoader.displayImage(statusVo.getOriginal_pic(), pic,
						options);

			} else if (statusVo.getBmiddle_pic() != null) {
				pic.setVisibility(View.VISIBLE);

				imageLoader.displayImage(statusVo.getBmiddle_pic(), pic,
						options);
			} else if (statusVo.getThumbnail_pic() != null) {
				pic.setVisibility(View.VISIBLE);

				imageLoader.displayImage(statusVo.getThumbnail_pic(), pic,
						options);
			} else {
				pic.setVisibility(View.GONE);

			}
			time.setText(TimeUtil.converTime(new Date(statusVo.getCreated_at())
					.getTime() / 1000));
			replay.setText("回复" + "("
					+ String.valueOf(statusVo.getComments_count()) + ")");
			forward.setText("转发" + "("
					+ String.valueOf(statusVo.getReposts_count()) + ")");
			source.setText("来自：" + Html.fromHtml(statusVo.getSource()));

			if (statusVo.getRetweeted_status() != null) {
				retweeted_layout.setVisibility(View.VISIBLE);
				Log.d("RESULT", (retweeted_msg == null) + "msg");
				retweeted_msg.setText(((statusVo.getRetweeted_status()
						.getUser() == null) ? "" : (statusVo
						.getRetweeted_status().getUser().getName() + ":  "))
						+ (statusVo.getRetweeted_status().getText()));

				retweeted_replay.setText("回复"
						+ "("
						+ String.valueOf(statusVo.getRetweeted_status()
								.getComments_count()) + ")");
				retweeted_forward.setText("转发"
						+ "("
						+ String.valueOf(statusVo.getRetweeted_status()
								.getReposts_count()) + ")");

				if (statusVo.getRetweeted_status().getOriginal_pic() != null) {
					retweeted_pic.setVisibility(View.VISIBLE);
					imageLoader.displayImage(statusVo.getRetweeted_status()
							.getOriginal_pic(), retweeted_pic, options);
				} else if (statusVo.getRetweeted_status().getBmiddle_pic() != null) {
					retweeted_pic.setVisibility(View.VISIBLE);

					imageLoader.displayImage(statusVo.getRetweeted_status()
							.getBmiddle_pic(), retweeted_pic, options);
				} else if (statusVo.getRetweeted_status().getThumbnail_pic() != null) {
					retweeted_pic.setVisibility(View.VISIBLE);

					imageLoader.displayImage(statusVo.getRetweeted_status()
							.getThumbnail_pic(), retweeted_pic, options);
				} else {
					retweeted_pic.setVisibility(View.GONE);
				}
				retweeted_layout
						.setOnClickListener(new RelativeLayout.OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(
										"com.loftcat.ui.TheStatus");
								Bundle budBundle = new Bundle();
								budBundle.putSerializable("statusVo",
										statusVo.getRetweeted_status());
								intent.putExtra("status", budBundle);
								startActivity(intent);
							}
						});
			} else {
				retweeted_layout.setVisibility(View.GONE);
			}
		}
	};
}
