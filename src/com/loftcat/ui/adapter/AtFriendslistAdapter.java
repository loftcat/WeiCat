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
package com.loftcat.ui.adapter;

import java.util.ArrayList;

import com.loftcat.R;
import com.loftcat.utils.log.LogCenter;
import com.loftcat.weibo.vo.UserVO;
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
