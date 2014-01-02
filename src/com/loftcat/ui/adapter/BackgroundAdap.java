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

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.utils.Utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BackgroundAdap extends BaseAdapter {
	Context context;
	int selectedId = 0;

	public BackgroundAdap(Context context) {
		this.context = context;
		selectedId = Utility.getBackgroundId(context);
		Log.d("RESULT", "BackgroundAdap selectedId: " + selectedId);
	}

	@Override
	public int getCount() {
		return AppConfig.backgrounds.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(
				R.layout.setting_setbackground_item, null);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.setting_setbackground_griditem);
		imageView.setBackgroundResource(AppConfig.backgrounds[position]
				.getSmallImage());
		RelativeLayout isselect = (RelativeLayout) convertView
				.findViewById(R.id.setting_setbackground_griditem_isselect);
		if (selectedId == position) {
			isselect.setVisibility(View.VISIBLE);
		} else {
			isselect.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		selectedId = Utility.getBackgroundId(context);
		super.notifyDataSetChanged();

	}

}
