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
import android.app.Activity;

import com.loftcat.R;
import com.loftcat.weibo.vo.GroupsVo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupsGalleryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<GroupsVo> groups;

	public GroupsGalleryAdapter(Context context, ArrayList<GroupsVo> groups) {
		this.context = context;
		this.groups = groups;
	}

	public void reSetData(ArrayList<GroupsVo> groups) {
		this.groups = groups;
		Log.d("RESULT", "reSetData:" + groups.size());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groups.size();
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

		View v = ((Activity) context).getLayoutInflater().inflate(
				R.layout.gallery_item, null);
		TextView textView = (TextView) v.findViewById(R.id.gallery_item_textview);
		textView.setText(groups.get(arg0).getName());
		return v;
	}

}
