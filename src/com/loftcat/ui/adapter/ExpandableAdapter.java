package com.loftcat.ui.adapter;

import java.util.ArrayList;
import java.util.Date;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.ui.Homepage;
import com.loftcat.utils.TimeUtil;
import com.loftcat.weibo.bean.StatusVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: ExpandableAdapter.java
 * @Description:操控常识介绍适配器
 * @see CKHelpAty
 * @Author: heb
 * @CreateDate: 2012/09/04
 * @Version: v1.1
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {
	private Context context;
	ArrayList<StatusVo> statusVos;
	private DisplayImageOptions options;

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	public void reSetData(ArrayList<StatusVo> statusVos) {
		this.statusVos = statusVos;
	}

	public ExpandableAdapter(ArrayList<StatusVo> statusVos, Context context) {
		this.statusVos = statusVos;
		this.context = context;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.heart)
				.showImageForEmptyUri(R.drawable.head_default).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(0)).build();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v = null;
		CViewHolder cvHolder = null;
		if (convertView != null) {
			v = convertView;
			cvHolder = (CViewHolder) v.getTag();
		} else {
			v = ((Activity) context).getLayoutInflater().inflate(
					R.layout.status_item_child, null);
			cvHolder = new CViewHolder();
			cvHolder.forward = (TextView) v.findViewById(R.id.forward);
			cvHolder.comment = (TextView) v.findViewById(R.id.comment);
			v.setTag(cvHolder);
		}
		cvHolder.forward.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppConfig.INTENT_ACTION_SEND);
				intent.putExtra("userID", statusVos.get(groupPosition).getUser().getId());
				intent.putExtra("status_id", statusVos.get(groupPosition).getId());
				intent.putExtra("mode", "forward");
				context.startActivity(intent);
			}
		});

		cvHolder.comment.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppConfig.INTENT_ACTION_SEND);
				intent.putExtra("userID", statusVos.get(groupPosition).getUser().getId());
				intent.putExtra("status_id", statusVos.get(groupPosition).getId());
				intent.putExtra("mode", "comment");
				context.startActivity(intent);
			}
		});

		return v;
	}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public Object getGroup(int groupPosition) {
		return null;
	}

	public int getGroupCount() {
		return statusVos.size();
	}

	public long getGroupId(int groupPosition) {
		return 0;
	}

	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View v = null;
		ViewHolder viewHolder;
		if (convertView != null) {
			v = convertView;
			viewHolder = (ViewHolder) v.getTag();
		} else {
			v = ((Activity) context).getLayoutInflater().inflate(
					R.layout.status_item_parent, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) v
					.findViewById(R.id.status_item_parent_name);
			viewHolder.msg = (TextView) v
					.findViewById(R.id.status_item_parent_msg);
			viewHolder.pic = (ImageView) v
					.findViewById(R.id.status_item_parent_pic);
			viewHolder.retweeted_msg = (TextView) v
					.findViewById(R.id.status_item_parent_retweeted_status_text);
			viewHolder.retweeted_replay = (TextView) v
					.findViewById(R.id.status_item_parent_retweeted_status_reply);
			viewHolder.retweeted_forward = (TextView) v
					.findViewById(R.id.status_item_parent_retweeted_status_forward);
			viewHolder.retweeted_layout = (RelativeLayout) v
					.findViewById(R.id.status_item_parent_retweeted_layout);
			viewHolder.retweeted_pic = (ImageView) v
					.findViewById(R.id.status_item_parent_retweeted_status_pic);
			viewHolder.relativeLayout = (RelativeLayout) v
					.findViewById(R.id.status_item_layout);

			viewHolder.head = (ImageView) v
					.findViewById(R.id.status_item_parent_head_imageview);
			viewHolder.time = (TextView) v
					.findViewById(R.id.status_item_parent_time);
			viewHolder.source = (TextView) v
					.findViewById(R.id.status_item_parent_source);
			viewHolder.replay = (TextView) v
					.findViewById(R.id.status_item_parent_reply);
			viewHolder.forward = (TextView) v
					.findViewById(R.id.status_item_parent_forward);
			v.setTag(viewHolder);
		}
		 viewHolder.name.setText(statusVos.get(groupPosition).getUser()
		 .getName());
		 viewHolder.msg.setText(statusVos.get(groupPosition).getText());

		if (statusVos.get(groupPosition).getThumbnail_pic() != null) {
			viewHolder.pic.setVisibility(View.VISIBLE);

			imageLoader.displayImage(statusVos.get(groupPosition)
					.getThumbnail_pic(), viewHolder.pic, options);
		} else {
			viewHolder.pic.setVisibility(View.GONE);

		}

		imageLoader.displayImage(statusVos.get(groupPosition).getUser()
				.getProfile_image_url(), viewHolder.head, options);
		viewHolder.time.setText(TimeUtil.converTime(new Date(statusVos.get(
				groupPosition).getCreated_at()).getTime() / 1000));
		viewHolder.replay.setText("回复"
				+ "("
				+ String.valueOf(statusVos.get(groupPosition)
						.getComments_count()) + ")");
		viewHolder.forward.setText("转发"
				+ "("
				+ String.valueOf(statusVos.get(groupPosition)
						.getReposts_count()) + ")");
		viewHolder.source.setText(Html.fromHtml(statusVos.get(groupPosition)
				.getSource()));

		if (statusVos.get(groupPosition).getRetweeted_status() != null) {
			viewHolder.retweeted_layout.setVisibility(View.VISIBLE);
			viewHolder.retweeted_msg.setText(((statusVos.get(groupPosition)
					.getRetweeted_status().getUser() == null) ? "" : (statusVos
					.get(groupPosition).getRetweeted_status().getUser()
					.getName() + ":  "))
					+ (statusVos.get(groupPosition).getRetweeted_status()
							.getText()));

			viewHolder.retweeted_replay.setText("回复("
					+ String.valueOf(statusVos.get(groupPosition)
							.getRetweeted_status().getComments_count()) + ")");
			viewHolder.retweeted_forward.setText("转发("
					+ String.valueOf(statusVos.get(groupPosition)
							.getRetweeted_status().getReposts_count()) + ")");

			if (statusVos.get(groupPosition).getRetweeted_status()
					.getThumbnail_pic() != null) {
				viewHolder.retweeted_pic.setVisibility(View.VISIBLE);
				imageLoader.displayImage(statusVos.get(groupPosition)
						.getRetweeted_status().getThumbnail_pic(),
						viewHolder.retweeted_pic, options);
			} else {
				viewHolder.retweeted_pic.setVisibility(View.GONE);
			}

			viewHolder.retweeted_layout
					.setOnClickListener(new RelativeLayout.OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									"com.loftcat.ui.TheStatus");
							Bundle budBundle = new Bundle();
							budBundle.putSerializable("statusVo", statusVos
									.get(groupPosition).getRetweeted_status());
							intent.putExtra("status", budBundle);
							context.startActivity(intent);
						}
					});

		} else {
			viewHolder.retweeted_layout.setVisibility(View.GONE);
		}
		viewHolder.relativeLayout
				.setOnClickListener(new RelativeLayout.OnClickListener() {

					@Override
					public void onClick(View x) {
						Intent intent = new Intent("com.loftcat.ui.TheStatus");
						Bundle budBundle = new Bundle();
						budBundle.putSerializable("statusVo",
								statusVos.get(groupPosition));
						intent.putExtra("status", budBundle);
						context.startActivity(intent);
					}
				});
		viewHolder.replay.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (statusVos.get(groupPosition).getComments_count() == 0) {
					Toast.makeText(context, "该微博还没有回复~", Toast.LENGTH_LONG)
							.show();
				} else {
					Intent intent = new Intent(AppConfig.INTENT_ACTION_COMMENTS);
					intent.putExtra("mode", "the_comments");
					intent.putExtra("status_id", statusVos.get(groupPosition)
							.getId());
					context.startActivity(intent);
				}
			}

		});
		viewHolder.forward.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (statusVos.get(groupPosition).getComments_count() == 0) {
					Toast.makeText(context, "该微博还没有转发~", Toast.LENGTH_LONG)
							.show();
				} else {
					Intent intent = new Intent(AppConfig.INTENT_ACTION_COMMENTS);
					intent.putExtra("mode", "reports");
					intent.putExtra("status_id", statusVos.get(groupPosition)
							.getId());
					context.startActivity(intent);
				}
			}
		});

		viewHolder.head.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(
						"userID",
						String.valueOf(statusVos.get(groupPosition).getUser()
								.getId()));
				intent.setAction(AppConfig.INTENT_ACTION_SELFPAGE);
				context.startActivity(intent);
			}
		});
		return v;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	class ViewHolder {
		TextView name;
		TextView msg;
		ImageView pic;
		TextView retweeted_msg;
		TextView retweeted_replay;
		TextView retweeted_forward;
		ImageView retweeted_pic;
		RelativeLayout retweeted_layout;
		ImageView head;
		TextView time;
		TextView source;
		TextView replay;
		TextView forward;
		RelativeLayout relativeLayout;

	}

	class CViewHolder {
		TextView forward;
		TextView comment;
	}

}
