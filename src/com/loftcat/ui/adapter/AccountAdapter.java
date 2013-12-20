package com.loftcat.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.loftcat.R;
import com.loftcat.weibo.bean.Account;

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
	private List<Account> accounts;
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

	public AccountAdapter(Context context, List<Account> accounts, Long index) {
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
