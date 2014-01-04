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
import java.util.List;

import com.loftcat.R;
import com.loftcat.weibo.vo.AccountVo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	private Context context;
	private List<AccountVo> accounts;
	private List<ImageView> imagelists;
	private long index;

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public List<ImageView> getImagelists() {
		return imagelists;
	}

	public void setImagelists(List<ImageView> imagelists) {
		this.imagelists = imagelists;
	}

	public AccountAdapter(Context context, List<AccountVo> accounts, Long index) {
		this.context = context;
		this.accounts = accounts;
		imagelists = new ArrayList<ImageView>();
		this.index = index;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accounts.size();
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
				R.layout.left_adapter, null);
		TextView TextView = (android.widget.TextView) v
				.findViewById(R.id.textView1);
		TextView.setText(accounts.get(arg0).getName());
		ImageView imageView = (ImageView) v.findViewById(R.id.imageView1);
		if (Long.valueOf(accounts.get(arg0).getId()) == getIndex()) {
			imageView.setVisibility(View.VISIBLE);
			Log.d("RESULT", accounts.get(arg0).getId() + "------" + getIndex());
		}
		imagelists.add(imageView);
		return v;
	}

}
