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
package com.loftcat.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.loftcat.weibo.bean.Account;

/**
 * 数据库管理类
 * 
 * @author HeBin
 * 
 * @version 1.0
 * 
 */
public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase mSQLiteDatabase;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		mSQLiteDatabase = helper.getWritableDatabase();
	}

	/**
	 * add accounts
	 * 
	 * @param accounts
	 */
	public void add(Account account) {
		mSQLiteDatabase.beginTransaction(); // 开始事务
		try {
			mSQLiteDatabase.execSQL(
					"INSERT INTO account VALUES(null, ?, ?, ?, ?, ?)",
					new Object[] { account.getId(), account.getName(),
							account.getUserInfo(), account.getToken(),
							account.getExpires_in() });
			mSQLiteDatabase.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			mSQLiteDatabase.endTransaction(); // 结束事务
		}
	}

	/**
	 * close database
	 */
	public void closeDB() {
		mSQLiteDatabase.close();
	}

	/**
	 * delete one account
	 * 
	 * @param account
	 */
	public void deleteAccount(Account account) {

		mSQLiteDatabase.delete("account", "id= ?",
				new String[] { account.getId() });

	}

	/**
	 * delete accounts
	 * 
	 * @param accounts
	 */
	public void deleteAccounts(ArrayList<Account> accounts) {
		for (Account account : accounts) {
			mSQLiteDatabase.delete("account", "id= ?",
					new String[] { account.getId() });
		}
	}

	/**
	 * query all accounts, return list
	 * 
	 * @return List<Account>
	 */
	public List<Account> getAccounts() {
		ArrayList<Account> accounts = new ArrayList<Account>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			Account account = new Account();
			// account.set = c.getInt(c.getColumnIndex("_id"));
			account.setId(c.getString(c.getColumnIndex("id")));
			account.setName(c.getString(c.getColumnIndex("name")));
			account.setUserInfo(c.getString(c.getColumnIndex("userInfo")));
			account.setToken(c.getString(c.getColumnIndex("token")));
			account.setExpires_in(c.getString(c.getColumnIndex("expires_in")));
			accounts.add(account);
			uids.add(c.getString(c.getColumnIndex("id")));
		}
		c.close();
		return accounts;
	}

	/**
	 * query all accounts, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor() {
		Cursor c = mSQLiteDatabase.rawQuery("SELECT * FROM account", null);
		return c;
	}

	/**
	 * update account's name
	 * 
	 * @param account
	 */
	public void updateName(Account account) {
		ContentValues cv = new ContentValues();
		cv.put("name", account.getName());
		mSQLiteDatabase.update("account", cv, "id = ?",
				new String[] { account.getId() });// 根据ID，更新用昵称
	}

	public void updateExpires_in(Account account) {
		ContentValues cv = new ContentValues();
		cv.put("expires_in", account.getExpires_in());
		mSQLiteDatabase.update("account", cv, "id = ?",
				new String[] { account.getId() });// 根据ID，更新用昵称
	}

	// public void updateTimeLine(String msg, String id) {
	// ContentValues cv = new ContentValues();
	// cv.put("timeline", msg);
	// mSQLiteDatabase.update("account", cv, "id = ?", new String[] { id });//
	// 根据ID，更新用昵称
	// }

	public List<String> getUids() {
		return uids;

	}

	private List<String> uids = new ArrayList<String>();
}
