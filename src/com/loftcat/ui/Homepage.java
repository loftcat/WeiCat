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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.gesture.GestureOverlayView;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.loftcat.R;
import com.loftcat.ui.utils.slidingmenu.fragment.LeftFragment;
import com.loftcat.ui.utils.slidingmenu.fragment.MiddleFragment;
import com.loftcat.ui.utils.slidingmenu.fragment.RightFragment;
import com.loftcat.ui.utils.slidingmenu.view.SlidingMenu;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.DBManager;
import com.loftcat.utils.LogCenter;
import com.loftcat.utils.Utility;
import com.loftcat.weibo.bean.Account;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;

public class Homepage extends BaseActivity {
	private SlidingMenu mSlidingMenu;
	private LeftFragment leftFragment;
	private RightFragment rightFragment;
	private MiddleFragment middleFragment;
	private long _index;
	private long since_id;

	public long getSince_id() {
		return since_id;
	}

	boolean isexit = false;
	Timer timer;

	@Override
	public void onBackPressed() {
		if (!isexit) {
			Toast.makeText(this, "再按一次返回退出程序！", Toast.LENGTH_SHORT).show();
			isexit = true;
			if (timer == null) {
				timer = new Timer();
			}
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isexit = false;
				}
			}, 1000);
		} else {
			super.onBackPressed();

		}
	}

	private int page = 1;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		middleFragment.setCount(page);
		this.page = page;
	}

	public void setSince_id(long since_id) {
		middleFragment.setSince_id(since_id);
		this.since_id = since_id;
	}

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

	List<Account> accounts;
	int x;

	@Override
	public void initView() {
		setContentView(R.layout.main);
		
		
		accounts = mDBManager.getAccounts();
		int size = accounts.size();
		_index = utility.readIndex();
		Log.d("RESULT", "_index:" + _index);
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (Long.valueOf(accounts.get(i).getId()) == _index) {
					_account = accounts.get(i);
					x = i;
				}
			}
		}
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(
				R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));
		mSlidingMenu.setCanSliding(true, false);
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);
		leftFragment.passContext(this);
		leftFragment.setUtility(utility);
		leftFragment.setAccounts(mDBManager, accounts);
		Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken(
				_account.getToken(), _account.getExpires_in());
		oauth2AccessToken.setExpiresIn(_account.getExpires_in());
		utility.setAccessToken(oauth2AccessToken);
		middleFragment = new MiddleFragment();
		middleFragment.setUtility(utility);
		t.replace(R.id.center_frame, middleFragment);
		middleFragment.passContext(this);
		middleFragment.setAccount(_account);

		t.commit();
	}

	@Override
	public void initLogic() {
		MobclickAgent.onError(this);
		UmengUpdateAgent.update(this);
	}

	@Override
	public void initListener() {

	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showCenterView();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}

	};

	public void setAccount(Account account) {
		_account = account;
		middleFragment.setAccount(_account);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDBManager.closeDB();// this mDBManager serves for sina and tencent
	}

}
