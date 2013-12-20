package com.loftcat.ui.adapter;

import java.util.ArrayList;

import com.loftcat.R;
import com.loftcat.utils.LogCenter;
import com.loftcat.weibo.bean.UserVO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AtFriendslistAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<UserVO> userVOs;

	public AtFriendslistAdapter(Context context, ArrayList<UserVO> userVOs) {
		this.userVOs = userVOs;
		this.context = context;
		Log.d("RESULT", userVOs.size() + "size");
	}

	public void reSetData(ArrayList<UserVO> userVOs) {
		this.userVOs = userVOs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userVOs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = ((Activity) context).getLayoutInflater().inflate(
					R.layout.frienditem_2, null);
		}
		TextView msg = (TextView) arg1.findViewById(R.id.frienditem_msg);
		msg.setText(userVOs.get(arg0).getName());
		return arg1;
	}

}
